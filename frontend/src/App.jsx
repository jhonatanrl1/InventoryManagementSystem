import { useEffect, useState } from "react";
import "./App.css";

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


  useEffect(() => {
    const url = lowStock
      ? "http://localhost:8080/products?lowStock=true"
      : `http://localhost:8080/products?search=${search}`;

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
          setProducts(data);
          setLoading(false);
        })
        .catch(() => {
          setError("Unable to load products.");
          setLoading(false);
        });
  }, [search, lowStock]);


useEffect(() => {
  fetch("http://localhost:8080/inventory/value")
    .then((response) => response.json())
    .then((data) => setInventoryValue(data));
}, []);



function handleAddProduct(event) {
  event.preventDefault();

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
      setSearch("");
      setLowStock(false);
    })
    .catch(() => {
      setError("Unable to add product. Check the information and try again.");
    });
}






  return (
    <div className="app">
      <h1>Inventory Management System</h1>


    <h2>Add Product</h2>

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

      <h2>Products</h2>

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
        onChange={(event) => setSearch(event.target.value)}
      />


      <button onClick={() => setLowStock(!lowStock)}>
        {lowStock ? "Show All Products" : "Show Low Stock"}
      </button>

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
             <h3>{product.name}</h3>
             <p>{product.description}</p>
             <p>Price: ${product.sellingPrice}</p>
             <p>Quantity in stock: {product.quantityInStock}</p>
           </div>
         ))
       )}
     </div>


    </div>
  );
}

export default App;

