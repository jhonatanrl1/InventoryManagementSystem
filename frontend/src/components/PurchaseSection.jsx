import { useEffect, useState } from "react";

const API_URL = "http://localhost:8080";
const PAGE_SIZE = 2;

function PurchaseSection() {
  const [purchases, setPurchases] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");





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
    }, [currentPage]);






  return (
    <section className="purchase-section">
      <h2>Purchases</h2>






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
