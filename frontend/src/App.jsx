import { useEffect, useState } from "react";

function App() {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/products")
      .then((response) => response.json())
      .then((data) => setProducts(data));
  }, []);

  return (
    <div>
      <h1>Inventory Management System</h1>

      <h2>Products</h2>

      {products.map((product) => (
        <div key={product.productId}>
          <h3>{product.name}</h3>
          <p>Price: ${product.sellingPrice}</p>
          <p>Quantity in stock: {product.quantityInStock}</p>
        </div>
      ))}
    </div>
  );
}


export default App;

