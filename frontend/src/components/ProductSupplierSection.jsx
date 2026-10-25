import { useEffect, useState } from "react";

const API_URL = "http://localhost:8080";
const PAGE_SIZE = 2;

function ProductSupplierSection() {
  const [products, setProducts] = useState([]);
  const [suppliers, setSuppliers] = useState([]);

  const [productSearch, setProductSearch] = useState("");
  const [supplierSearch, setSupplierSearch] = useState("");

  const [productPage, setProductPage] = useState(0);
  const [supplierPage, setSupplierPage] = useState(0);

  const [productTotalPages, setProductTotalPages] = useState(0);
  const [supplierTotalPages, setSupplierTotalPages] = useState(0);

  const [productsLoading, setProductsLoading] = useState(true);
  const [suppliersLoading, setSuppliersLoading] = useState(true);

  const [productsError, setProductsError] = useState("");
  const [suppliersError, setSuppliersError] = useState("");



  const [selectedProductId, setSelectedProductId] = useState("");
  const [selectedSupplierId, setSelectedSupplierId] = useState("");
  const [purchasePrice, setPurchasePrice] = useState("");

  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState("");
  const [saveSuccess, setSaveSuccess] = useState("");



  const [associations, setAssociations] = useState([]);
  const [associationsLoading, setAssociationsLoading] = useState(true);
  const [associationsError, setAssociationsError] = useState("");


async function handleCreateAssociation(event) {
  event.preventDefault();

  setSaveError("");
  setSaveSuccess("");

  if (!selectedProductId || !selectedSupplierId) {
    setSaveError("Please select both a product and a supplier.");
    return;
  }

  if (
    purchasePrice.trim() === "" ||
    !Number.isFinite(Number(purchasePrice)) ||
    Number(purchasePrice) < 0
  ) {
    setSaveError("Enter a valid purchase price of zero or greater.");
    return;
  }

  try {
    setSaving(true);

    const response = await fetch(`${API_URL}/product-suppliers`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        product: {
          productId: Number(selectedProductId),
        },
        supplier: {
          supplierId: Number(selectedSupplierId),
        },
        purchasePrice: Number(purchasePrice),
      }),
    });

    if (!response.ok) {
      const message = await response.text();
      throw new Error(message || "Failed to create association.");
    }

    setSaveSuccess("Product-supplier association created successfully.");
    setPurchasePrice("");


    const associationsResponse = await fetch(
      `${API_URL}/product-suppliers`
    );

    if (!associationsResponse.ok) {
      throw new Error(
        "Association was created, but the list could not be refreshed."
      );
    }

    const associationsData = await associationsResponse.json();
    setAssociations(associationsData);




  } catch (err) {
    setSaveError(err.message || "Unable to create association.");
  } finally {
    setSaving(false);
  }
}



  // Load one page of products and search on the server.
  useEffect(() => {
    const controller = new AbortController();

    const timeoutId = setTimeout(async () => {
      try {
        setProductsLoading(true);
        setProductsError("");

        const params = new URLSearchParams({
          page: String(productPage),
          size: String(PAGE_SIZE),
        });

        if (productSearch.trim()) {
          params.set("search", productSearch.trim());
        }

        const response = await fetch(
          `${API_URL}/products?${params.toString()}`,
          { signal: controller.signal }
        );

        if (!response.ok) {
          throw new Error("Failed to load products.");
        }

        const data = await response.json();

        setProducts(data.content ?? []);
        setProductTotalPages(data.totalPages ?? 0);
      } catch (err) {
        if (err.name !== "AbortError") {
          setProductsError("Unable to load products.");
        }
      } finally {
        if (!controller.signal.aborted) {
          setProductsLoading(false);
        }
      }
    }, 300);

    return () => {
      clearTimeout(timeoutId);
      controller.abort();
    };
  }, [productSearch, productPage]);

  // Load one page of suppliers and search on the server.
  useEffect(() => {
    const controller = new AbortController();

    const timeoutId = setTimeout(async () => {
      try {
        setSuppliersLoading(true);
        setSuppliersError("");

        const params = new URLSearchParams({
          page: String(supplierPage),
          size: String(PAGE_SIZE),
        });

        if (supplierSearch.trim()) {
          params.set("search", supplierSearch.trim());
        }

        const response = await fetch(
          `${API_URL}/suppliers?${params.toString()}`,
          { signal: controller.signal }
        );

        if (!response.ok) {
          throw new Error("Failed to load suppliers.");
        }

        const data = await response.json();

        setSuppliers(data.content ?? []);
        setSupplierTotalPages(data.totalPages ?? 0);
      } catch (err) {
        if (err.name !== "AbortError") {
          setSuppliersError("Unable to load suppliers.");
        }
      } finally {
        if (!controller.signal.aborted) {
          setSuppliersLoading(false);
        }
      }
    }, 300);

    return () => {
      clearTimeout(timeoutId);
      controller.abort();
    };
  }, [supplierSearch, supplierPage]);




// Load existing product-supplier associations.
useEffect(() => {
  const controller = new AbortController();

  async function loadAssociations() {
    try {
      setAssociationsLoading(true);
      setAssociationsError("");

      const response = await fetch(
        `${API_URL}/product-suppliers`,
        { signal: controller.signal }
      );

      if (!response.ok) {
        throw new Error("Failed to load product-supplier associations.");
      }

      const data = await response.json();

      setAssociations(data);
    } catch (err) {
      if (err.name !== "AbortError") {
        setAssociationsError(
          "Unable to load product-supplier associations."
        );
      }
    } finally {
      if (!controller.signal.aborted) {
        setAssociationsLoading(false);
      }
    }
  }

  loadAssociations();

  return () => controller.abort();
}, []);









  return (
    <section className="product-supplier-section">
      <h2>Product-Suppliers</h2>

      <p>
        Search for products and suppliers to prepare their associations.
      </p>

      <div className="product-selector">
        <h3>Find a Product</h3>

        <label htmlFor="product-search">Search products</label>
        <input
          id="product-search"
          type="search"
          placeholder="Search by product name..."
          value={productSearch}
          onChange={(event) => {
            setProductSearch(event.target.value);
            setProductPage(0);
          }}
        />

        {productsLoading && <p>Loading products...</p>}
        {productsError && <p role="alert">{productsError}</p>}

        {!productsLoading && !productsError && (
          <>
            <label htmlFor="product-select">Select product</label>


           <select
             id="product-select"
             value={selectedProductId}
             onChange={(event) => setSelectedProductId(event.target.value)}
           >
             <option value="">Choose a product</option>

             {products.map((product) => (
               <option key={product.productId} value={product.productId}>
                 {product.name} (ID: {product.productId})
               </option>
             ))}
           </select>


            {products.length === 0 && <p>No products found.</p>}

            <div className="pagination">
              <button
                type="button"
                disabled={productPage === 0}
                onClick={() => setProductPage((page) => page - 1)}
              >
                Previous
              </button>

              <span>
                Page {productTotalPages === 0 ? 0 : productPage + 1} of{" "}
                {productTotalPages}
              </span>

              <button
                type="button"
                disabled={productPage + 1 >= productTotalPages}
                onClick={() => setProductPage((page) => page + 1)}
              >
                Next
              </button>
            </div>
          </>
        )}
      </div>

      <hr />

      <div className="supplier-selector">
        <h3>Find a Supplier</h3>

        <label htmlFor="supplier-search">Search suppliers</label>
        <input
          id="supplier-search"
          type="search"
          placeholder="Search by name, contact, email, or phone..."
          value={supplierSearch}
          onChange={(event) => {
            setSupplierSearch(event.target.value);
            setSupplierPage(0);
          }}
        />

        {suppliersLoading && <p>Loading suppliers...</p>}
        {suppliersError && <p role="alert">{suppliersError}</p>}

        {!suppliersLoading && !suppliersError && (
          <>
            <label htmlFor="supplier-select">Select supplier</label>



            <select
              id="supplier-select"
              value={selectedSupplierId}
              onChange={(event) => setSelectedSupplierId(event.target.value)}
            >
              <option value="">Choose a supplier</option>

              {suppliers.map((supplier) => (
                <option key={supplier.supplierId} value={supplier.supplierId}>
                  {supplier.name} (ID: {supplier.supplierId})
                </option>
              ))}
            </select>


            {suppliers.length === 0 && <p>No suppliers found.</p>}

            <div className="pagination">
              <button
                type="button"
                disabled={supplierPage === 0}
                onClick={() => setSupplierPage((page) => page - 1)}
              >
                Previous
              </button>

              <span>
                Page {supplierTotalPages === 0 ? 0 : supplierPage + 1} of{" "}
                {supplierTotalPages}
              </span>

              <button
                type="button"
                disabled={supplierPage + 1 >= supplierTotalPages}
                onClick={() => setSupplierPage((page) => page + 1)}
              >
                Next
              </button>
            </div>
          </>
        )}
      </div>



      <hr />

      <h3>Create Product-Supplier Association</h3>

      <form onSubmit={handleCreateAssociation}>
        <label htmlFor="purchase-price">Supplier purchase price</label>

        <input
          id="purchase-price"
          type="number"
          min="0"
          step="0.01"
          value={purchasePrice}
          onChange={(event) => setPurchasePrice(event.target.value)}
          placeholder="Enter purchase price"
          required
        />

        <button type="submit" disabled={saving}>
          {saving ? "Saving..." : "Create Association"}
        </button>
      </form>

      {saveError && <p role="alert">{saveError}</p>}
      {saveSuccess && <p role="status">{saveSuccess}</p>}




      <hr />

      <h3>Existing Product-Supplier Associations</h3>

      {associationsLoading && <p>Loading associations...</p>}

      {associationsError && (
        <p role="alert">{associationsError}</p>
      )}

      {!associationsLoading &&
        !associationsError &&
        associations.length === 0 && (
          <p>No product-supplier associations found.</p>
        )}

      {!associationsLoading &&
        !associationsError &&
        associations.length > 0 && (
          <table>
            <thead>
              <tr>
                <th>Product ID</th>
                <th>Product Name</th>
                <th>Supplier ID</th>
                <th>Supplier Name</th>
                <th>Purchase Price</th>
              </tr>
            </thead>

            <tbody>
              {associations.map((association) => (
                <tr
                  key={`${association.product?.productId}-${association.supplier?.supplierId}`}
                >
                  <td>{association.product?.productId ?? "N/A"}</td>
                  <td>{association.product?.name ?? "N/A"}</td>
                  <td>{association.supplier?.supplierId ?? "N/A"}</td>
                  <td>{association.supplier?.name ?? "N/A"}</td>
                  <td>
                    {association.purchasePrice != null
                      ? Number(association.purchasePrice).toFixed(2)
                      : "N/A"}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}




    </section>
  );
}

export default ProductSupplierSection;

