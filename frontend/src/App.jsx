import { useEffect, useState } from "react";
import "./App.css";

import SupplierSection from "./components/SupplierSection";


import ProductSupplierSection from "./components/ProductSupplierSection";

import PurchaseSection from "./components/PurchaseSection";

function App() {
  const [products, setProducts] = useState([]);
  const [search, setSearch] = useState("");
  const [lowStock, setLowStock] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [inventoryValue, setInventoryValue] = useState(null);

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [sellingPrice, setSellingPrice] = useState("");

  const [quantityInStock, setQuantityInStock] = useState("");
  const [lowStockThreshold, setLowStockThreshold] = useState("");


  const [editingProductId, setEditingProductId] = useState(null);
  const [editName, setEditName] = useState("");
  const [editDescription, setEditDescription] = useState("");
  const [editSellingPrice, setEditSellingPrice] = useState("");
  const [editQuantityInStock, setEditQuantityInStock] = useState("");
  const [editLowStockThreshold, setEditLowStockThreshold] = useState("");



  const [deleteProductId, setDeleteProductId] = useState(null);
  const [deleteProductName, setDeleteProductName] = useState("");




  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);



const [refreshTrigger, setRefreshTrigger] = useState(0);


const [activeSection, setActiveSection] = useState("products");



  useEffect(() => {
    const params = new URLSearchParams();

    params.set("page", currentPage);
    params.set("size", "10");



    if (lowStock) {
      params.set("lowStock", "true");
    }

    if (search.trim() !== "") {
      params.set("search", search.trim());
    }



    const url = `http://localhost:8080/products?${params.toString()}`;

    setLoading(true);
    setError("");

    fetch(url)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to load products");
        }

        return response.json();
      })




    .then((data) => {
      setTotalPages(data.totalPages);

      if (data.totalPages === 0) {
        setProducts([]);
        setCurrentPage(0);
        setLoading(false);
        return;
      }

      if (currentPage >= data.totalPages) {
        setCurrentPage(data.totalPages - 1);
        return;
      }

      setProducts(data.content);
      setCurrentPage(data.number);
      setLoading(false);
    })






      .catch(() => {
        setError("Unable to load products.");
        setLoading(false);
      });
  }, [search, lowStock, currentPage, refreshTrigger]);






useEffect(() => {
  fetch("http://localhost:8080/inventory/value")
    .then((response) => response.json())
    .then((data) => setInventoryValue(data));
}, []);



function handleAddProduct(event) {
  event.preventDefault();
  setError("");

  fetch("http://localhost:8080/products", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      name: name,
      description: description,
      sellingPrice: Number(sellingPrice),
      quantityInStock: Number(quantityInStock),
      lowStockThreshold: Number(lowStockThreshold),
    }),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to add product");
      }

      return response.json();
    })

      .then(() => {
        setName("");
        setDescription("");
        setSellingPrice("");
        setQuantityInStock("");
        setLowStockThreshold("");

        setRefreshTrigger((count) => count + 1);

      })


    .catch(() => {
      setError("Unable to add product. Check the information and try again.");
    });
}


function handleEditProduct(product) {
  setEditingProductId(product.productId);
  setEditName(product.name);
  setEditDescription(product.description || "");
  setEditSellingPrice(product.sellingPrice);
  setEditQuantityInStock(product.quantityInStock);
  setEditLowStockThreshold(product.lowStockThreshold);
}


function handleUpdateProduct(event) {
  event.preventDefault();
  setError("");

  fetch(`http://localhost:8080/products/${editingProductId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      name: editName,
      description: editDescription,
      sellingPrice: Number(editSellingPrice),
      quantityInStock: Number(editQuantityInStock),
      lowStockThreshold: Number(editLowStockThreshold),
    }),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to update product");
      }
      return response.json();
    })



    .then(() => {
      setEditingProductId(null);
      setEditName("");
      setEditDescription("");
      setEditSellingPrice("");
      setEditQuantityInStock("");
      setEditLowStockThreshold("");

      setRefreshTrigger((count) => count + 1);

    })


    .catch(() => {
      setError("Unable to update product. Check the information and try again.");
    });
}



function handleDeleteProduct(productId) {
  setError("");

  fetch(`http://localhost:8080/products/${productId}`, {
    method: "DELETE",
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Failed to delete product");
      }

      setDeleteProductId(null);
      setDeleteProductName("");

      setRefreshTrigger((count) => count + 1);
    })
    .catch(() => {
      setError("Unable to delete product.");
    });
}






  return (
    <div className="app">
      <h1>Inventory Management System</h1>


     <div className="view-toggle">
       <button
         type="button"
         onClick={() => setActiveSection("products")}
       >
         Products
       </button>

       <button
         type="button"
         onClick={() => setActiveSection("suppliers")}
       >
         Suppliers
       </button>

       <button
         type="button"
         onClick={() => setActiveSection("product-suppliers")}
       >
         Product-Suppliers
       </button>



       <button
         type="button"
         onClick={() => setActiveSection("purchases")}
       >
         Purchases
       </button>

     </div>




  {activeSection === "products" && (
    <h2>Add Product</h2>
  )}


    {activeSection === "products" && (
      <form onSubmit={handleAddProduct}>

      <input
        type="text"
        placeholder="Product name"
        value={name}
        onChange={(event) => setName(event.target.value)}
        required
      />


    <input
      type="text"
      placeholder="Description (optional)"
      value={description}
      onChange={(event) => setDescription(event.target.value)}
    />


      <input
        type="number"
        placeholder="Selling price"
        value={sellingPrice}
        onChange={(event) => setSellingPrice(event.target.value)}
        min="0"
        step="0.01"
        required
      />



        <input
          type="number"
          placeholder="Quantity in stock"
          value={quantityInStock}
          onChange={(event) => setQuantityInStock(event.target.value)}
          min="0"
          required
        />

        <input
          type="number"
          placeholder="Low-stock threshold"
          value={lowStockThreshold}
          onChange={(event) => setLowStockThreshold(event.target.value)}
          min="0"
          required
        />


         <button type="submit">Add Product</button>

        </form>

        )}



{activeSection === "suppliers" && <SupplierSection />}


{activeSection === "product-suppliers" && (
  <ProductSupplierSection />
)}



{activeSection === "purchases" && <PurchaseSection />}



{activeSection === "products" && (
  <>




      {inventoryValue !== null && (
        <p>Inventory Value: ${inventoryValue}</p>
      )}

    {!loading && !error && products.length > 0 && (
      <p>Showing {products.length} product{products.length !== 1 ? "s" : ""}</p>
    )}



      <input
        type="text"
        placeholder="Search products..."
        value={search}
        onChange={(event) => {
          setSearch(event.target.value);
          setCurrentPage(0);
        }}
      />


      <button onClick={() => setLowStock(!lowStock)}>
        {lowStock ? "Show All Products" : "Show Low Stock"}
      </button>


      <h2>Products</h2>

        <div className="product-list">
          {loading ? (
            <p>Loading products...</p>
          ) : error ? (
            <p>{error}</p>
          ) : products.length === 0 ? (
            <p>No products found.</p>
          ) : (


         products.map((product) => (



           <div className="product-card" key={product.productId}>
             {editingProductId === product.productId ? (
               <form onSubmit={handleUpdateProduct}>
                   <h3>Edit Product</h3>


                 <label>
                   Product Name
                   <input
                     type="text"
                     value={editName}
                     onChange={(event) => setEditName(event.target.value)}
                     required
                   />
                 </label>

                 <label>
                   Description
                   <input
                     type="text"
                     value={editDescription}
                     onChange={(event) => setEditDescription(event.target.value)}
                   />
                 </label>

                 <label>
                   Selling Price
                   <input
                     type="number"
                     value={editSellingPrice}
                     onChange={(event) => setEditSellingPrice(event.target.value)}
                     min="0"
                     step="0.01"
                     required
                   />
                 </label>

                 <label>
                   Quantity in Stock
                   <input
                     type="number"
                     value={editQuantityInStock}
                     onChange={(event) => setEditQuantityInStock(event.target.value)}
                     min="0"
                     required
                   />
                 </label>

                 <label>
                   Low-Stock Threshold
                   <input
                     type="number"
                     value={editLowStockThreshold}
                     onChange={(event) => setEditLowStockThreshold(event.target.value)}
                     min="0"
                     required
                   />
                 </label>

                 <button type="submit">Save Changes</button>

                 <button
                   type="button"
                   onClick={() => setEditingProductId(null)}
                 >
                   Cancel
                 </button>
               </form>
             ) : (
               <>
                 <h3>{product.name}</h3>
                 <p>{product.description}</p>
                 <p>Price: ${product.sellingPrice}</p>
                 <p>Quantity in stock: {product.quantityInStock}</p>

                 <button onClick={() => handleEditProduct(product)}>
                   Edit
                 </button>


                 <button
                   onClick={() => {
                     setDeleteProductId(product.productId);
                     setDeleteProductName(product.name);
                   }}
                 >
                   Delete
                 </button>


               </>
             )}
           </div>



         ))
       )}
     </div>




     <div className="pagination-controls">
           <button
             type="button"
             onClick={() => setCurrentPage((page) => page - 1)}
             disabled={currentPage === 0 || loading}
           >
             Previous
           </button>

           <span>

            {totalPages === 0
              ? "No pages"
              : `Page ${currentPage + 1} of ${totalPages}`}

           </span>

           <button
             type="button"
             onClick={() => setCurrentPage((page) => page + 1)}
             disabled={currentPage >= totalPages - 1 || loading}
           >
             Next
           </button>
         </div>


{/* DELETE MODAL */}
    {deleteProductId !== null && (
      <div className="delete-modal-overlay">
        <div className="delete-modal">
          <h2>Delete Product</h2>

          <p>
  Are you sure you want to delete the product "{deleteProductName}"?
          </p>

          <div className="delete-modal-buttons">
            <button
              type="button"
              onClick={() => {
                setDeleteProductId(null);
                setDeleteProductName("");
              }}
            >
              Cancel
            </button>

            <button
              type="button"
              onClick={() => handleDeleteProduct(deleteProductId)}
            >
              Delete
            </button>
          </div>
        </div>
      </div>
    )}




      </>
    )}


    </div>






  );
}

export default App;

