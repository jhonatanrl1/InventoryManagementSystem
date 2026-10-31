import { useEffect, useState } from "react";

const API_URL = "http://localhost:8080";
const PAGE_SIZE = 2;

function PurchaseSection() {
  const [purchases, setPurchases] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");





   const [suppliers, setSuppliers] = useState([]);
   const [products, setProducts] = useState([]);
   const [selectedSupplierId, setSelectedSupplierId] = useState("");
   const [selectedProductId, setSelectedProductId] = useState("");
   const [quantity, setQuantity] = useState("1");
   const [purchasePrice, setPurchasePrice] = useState("");
   const [formError, setFormError] = useState("");
   const [formSuccess, setFormSuccess] = useState("");


   const [refreshTrigger, setRefreshTrigger] = useState(0);



  useEffect(() => {
      const controller = new AbortController();

      async function fetchPurchases() {
        setLoading(true);
        setError("");

        try {
          const response = await fetch(
            `${API_URL}/purchases?page=${currentPage}&size=${PAGE_SIZE}`,
            { signal: controller.signal }
          );

          if (!response.ok) {
            throw new Error("Unable to load purchases.");
          }

          const data = await response.json();

          setPurchases(data.content);
          setTotalPages(data.totalPages);
        } catch (err) {
          if (err.name !== "AbortError") {
            setError(err.message || "Unable to load purchases.");
          }
        } finally {
          if (!controller.signal.aborted) {
            setLoading(false);
          }
        }
      }

      fetchPurchases();

      return () => controller.abort();
    }, [currentPage, refreshTrigger]);





    useEffect(() => {
      const controller = new AbortController();

      async function fetchPurchaseOptions() {
        try {
          const [suppliersResponse, productsResponse] = await Promise.all([
            fetch(`${API_URL}/suppliers?page=0&size=100`, {
              signal: controller.signal,
            }),
            fetch(`${API_URL}/products?page=0&size=100`, {
              signal: controller.signal,
            }),
          ]);

          if (!suppliersResponse.ok || !productsResponse.ok) {
            throw new Error("Unable to load products and suppliers.");
          }

          const suppliersData = await suppliersResponse.json();
          const productsData = await productsResponse.json();

          setSuppliers(suppliersData.content);
          setProducts(productsData.content);
        } catch (err) {
          if (err.name !== "AbortError") {
            setFormError(err.message || "Unable to load products and suppliers.");
          }
        }
      }

      fetchPurchaseOptions();

      return () => controller.abort();
    }, []);







    async function handleCreatePurchase(event) {
      event.preventDefault();
      setFormError("");
      setFormSuccess("");

      if (!selectedSupplierId || !selectedProductId) {
        setFormError("Please select both a supplier and a product.");
        return;
      }

      const parsedQuantity = Number(quantity);
      const parsedPurchasePrice = Number(purchasePrice);

      if (!Number.isInteger(parsedQuantity) || parsedQuantity < 1) {
        setFormError("Quantity must be a whole number greater than 0.");
        return;
      }

      if (!Number.isFinite(parsedPurchasePrice) || parsedPurchasePrice < 0) {
        setFormError("Purchase price must be 0 or greater.");
        return;
      }

      try {
        const response = await fetch(`${API_URL}/purchases`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            supplier: {
              supplierId: Number(selectedSupplierId),
            },
            purchaseItems: [
              {
                product: {
                  productId: Number(selectedProductId),
                },
                quantity: parsedQuantity,
                purchasePrice: parsedPurchasePrice,
              },
            ],
          }),
        });

        if (!response.ok) {
          const message = await response.text();
          throw new Error(message || "Unable to create purchase.");
        }

        setFormSuccess("Purchase created successfully.");
        setSelectedSupplierId("");
        setSelectedProductId("");
        setQuantity("1");
        setPurchasePrice("");

        setCurrentPage(0);
        setRefreshTrigger((previous) => previous + 1);

      } catch (err) {
        setFormError(err.message || "Unable to create purchase.");
      }
    }






  return (
    <section className="purchase-section">
      <h2>Purchases</h2>





    <form
      className="purchase-form"
      onSubmit={handleCreatePurchase}
    >
      <h3>Create Purchase</h3>

      {formError && <p className="error">{formError}</p>}
      {formSuccess && <p>{formSuccess}</p>}

      <label>
        Supplier
        <select
          value={selectedSupplierId}
          onChange={(event) => setSelectedSupplierId(event.target.value)}
        >
          <option value="">Select a supplier</option>
          {suppliers.map((supplier) => (
            <option key={supplier.supplierId} value={supplier.supplierId}>
              {supplier.name}
            </option>
          ))}
        </select>
      </label>

      <label>
        Product
        <select
          value={selectedProductId}
          onChange={(event) => setSelectedProductId(event.target.value)}
        >
          <option value="">Select a product</option>
          {products.map((product) => (
            <option key={product.productId} value={product.productId}>
              {product.name}
            </option>
          ))}
        </select>
      </label>

      <label>
        Quantity
        <input
          type="number"
          min="1"
          step="1"
          value={quantity}
          onChange={(event) => setQuantity(event.target.value)}
        />
      </label>

      <label>
        Purchase price per unit
        <input
          type="number"
          min="0"
          step="0.01"
          value={purchasePrice}
          onChange={(event) => setPurchasePrice(event.target.value)}
        />
      </label>

      <button type="submit">Create Purchase</button>
    </form>









      {loading && <p>Loading purchases...</p>}

          {error && <p className="error">{error}</p>}

          {!loading && !error && purchases.length === 0 && (
            <p>No purchases found.</p>
          )}

          {!loading && !error && purchases.length > 0 && (
              <>
            <div>
              <table>
                <thead>
                  <tr>
                    <th>Purchase ID</th>
                    <th>Purchase Date</th>
                    <th>Supplier</th>
                    <th>Items</th>
                  </tr>
                </thead>

                <tbody>
                  {purchases.map((purchase) => (
                    <tr key={purchase.purchaseId}>
                      <td>{purchase.purchaseId}</td>
                      <td>
                        {purchase.purchaseDate
                          ? new Date(purchase.purchaseDate).toLocaleString()
                          : "Not provided"}
                      </td>
                      <td>
                        {purchase.supplier?.name ?? "Unknown supplier"}
                      </td>
                      <td>
                        {purchase.purchaseItems?.map((item) => (
                          <div key={item.purchaseItemId}>
                            {item.product?.name ?? "Unknown product"}
                            {" — Quantity: "}
                            {item.quantity}
                            {" — Price: $"}
                            {Number(item.purchasePrice).toFixed(2)}
                          </div>
                        ))}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>





             <div className="pagination">
                      <button
                        type="button"
                        onClick={() => setCurrentPage((page) => page - 1)}
                        disabled={currentPage === 0 || loading}
                      >
                        Previous
                      </button>

                      <span>
                        Page {totalPages === 0 ? 0 : currentPage + 1} of {totalPages}
                      </span>

                      <button
                        type="button"
                        onClick={() => setCurrentPage((page) => page + 1)}
                        disabled={
                          currentPage >= totalPages - 1 || loading || totalPages === 0
                        }
                      >
                        Next
                      </button>
                    </div>


            </>




          )}











    </section>
  );
}

export default PurchaseSection;
