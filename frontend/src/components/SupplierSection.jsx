import { useEffect, useState } from "react";

function SupplierSection() {
  const [supplierName, setSupplierName] = useState("");
  const [contactName, setContactName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [supplierError, setSupplierError] = useState("");
  const [suppliers, setSuppliers] = useState([]);

  const [editingSupplierId, setEditingSupplierId] = useState(null);
  const [editSupplierName, setEditSupplierName] = useState("");
  const [editContactName, setEditContactName] = useState("");
  const [editEmail, setEditEmail] = useState("");
  const [editPhone, setEditPhone] = useState("");

  const [deleteSupplierId, setDeleteSupplierId] = useState(null);
  const [deleteSupplierName, setDeleteSupplierName] = useState("");


useEffect(() => {
    fetch("http://localhost:8080/suppliers")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Unable to load suppliers.");
        }

        return response.json();
      })
      .then((data) => {
        setSuppliers(data);
        setSupplierError("");
      })
      .catch((error) => {
        setSupplierError(error.message);
      });
  }, []);




  const handleAddSupplier = async (event) => {
      event.preventDefault();
      setSupplierError("");

      const phoneDigits = phone.replace(/\D/g, "");

      if (phoneDigits.length < 7) {
        setSupplierError("Please enter a valid phone number.");
        return;
      }

      try {
        const response = await fetch("http://localhost:8080/suppliers", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            name: supplierName,
            contactName,
            email,
            phone,
          }),
        });

        if (!response.ok) {
          const message = await response.text();
          throw new Error(message || "Unable to add supplier.");
        }

        setSupplierName("");
        setContactName("");
        setEmail("");
        setPhone("");

        const suppliersResponse = await fetch(
          "http://localhost:8080/suppliers"
        );

        if (!suppliersResponse.ok) {
          throw new Error("Supplier was added, but the list could not be refreshed.");
        }

        const data = await suppliersResponse.json();
        setSuppliers(data);
        setSupplierError("");
      } catch (error) {
        setSupplierError(error.message || "Unable to add supplier. Check the information and try again.");
      }
    };



const handleUpdateSupplier = async (event) => {
    event.preventDefault();
    setSupplierError("");

    const phoneDigits = editPhone.replace(/\D/g, "");

    if (phoneDigits.length < 7) {
      setSupplierError("Please enter a valid phone number.");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/suppliers/${editingSupplierId}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            name: editSupplierName,
            contactName: editContactName,
            email: editEmail,
            phone: editPhone,
          }),
        }
      );

      if (!response.ok) {
        const message = await response.text();
        throw new Error(message || "Unable to update supplier.");
      }

      setEditingSupplierId(null);
      setEditSupplierName("");
      setEditContactName("");
      setEditEmail("");
      setEditPhone("");

      const suppliersResponse = await fetch(
        "http://localhost:8080/suppliers"
      );

      if (!suppliersResponse.ok) {
        throw new Error("Supplier was updated, but the list could not be refreshed.");
      }

      const data = await suppliersResponse.json();
      setSuppliers(data);
      setSupplierError("");
    } catch (error) {
      setSupplierError(
        error.message || "Unable to update supplier. Check the information and try again."
      );
    }
  };




const handleDeleteSupplier = async (supplierId) => {
    setSupplierError("");

    try {
      const response = await fetch(
        `http://localhost:8080/suppliers/${supplierId}`,
        {
          method: "DELETE",
        }
      );

      if (!response.ok) {
        const message = await response.text();
        throw new Error(message || "Unable to delete supplier.");
      }

      const suppliersResponse = await fetch(
        "http://localhost:8080/suppliers"
      );

      if (!suppliersResponse.ok) {
        throw new Error("Supplier was deleted, but the list could not be refreshed.");
      }

      const data = await suppliersResponse.json();
      setSuppliers(data);

      setDeleteSupplierId(null);
      setDeleteSupplierName("");
      setSupplierError("");
    } catch (error) {
      setSupplierError(
        error.message || "Unable to delete supplier."
      );

      setDeleteSupplierId(null);
      setDeleteSupplierName("");
    }
  };







return (
    <section>
      <h2>Suppliers</h2>

      <form onSubmit={handleAddSupplier}>


        <input
          type="text"
          placeholder="Supplier Name"
          value={supplierName}
          onChange={(event) => setSupplierName(event.target.value)}
          required
        />

        <input
          type="text"
          placeholder="Contact Name"
          value={contactName}
          onChange={(event) => setContactName(event.target.value)}
          required
        />

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
          required
        />

        <input
          type="tel"
          placeholder="Phone"
          value={phone}
          onChange={(event) => setPhone(event.target.value)}
          required
        />

        <button type="submit">Add Supplier</button>
      </form>

      {supplierError && (
        <p className="error-message">{supplierError}</p>
      )}

      <div className="supplier-list">
        {suppliers.map((supplier) => (
          <div
            key={supplier.supplierId}
            className="supplier-card"
          >
            {editingSupplierId === supplier.supplierId ? (
              <form onSubmit={handleUpdateSupplier}>
                <h3>Edit Supplier</h3>

                <input
                  type="text"
                  value={editSupplierName}
                  onChange={(event) =>
                    setEditSupplierName(event.target.value)
                  }
                  required
                />

                <input
                  type="text"
                  value={editContactName}
                  onChange={(event) =>
                    setEditContactName(event.target.value)
                  }
                  required
                />

                <input
                  type="email"
                  value={editEmail}
                  onChange={(event) =>
                    setEditEmail(event.target.value)
                  }
                  required
                />

                <input
                  type="tel"
                  value={editPhone}
                  onChange={(event) =>
                    setEditPhone(event.target.value)
                  }
                  required
                />

                <button type="submit">Save Changes</button>

                <button
                  type="button"
                  onClick={() => {
                    setEditingSupplierId(null);
                    setEditSupplierName("");
                    setEditContactName("");
                    setEditEmail("");
                    setEditPhone("");
                    setSupplierError("");
                  }}
                >
                  Cancel
                </button>
              </form>
            ) : (
              <>
                <h3>{supplier.name}</h3>
                <p>Contact: {supplier.contactName}</p>
                <p>Email: {supplier.email}</p>
                <p>Phone: {supplier.phone}</p>

                <button
                  type="button"
                  onClick={() => {
                    setEditingSupplierId(supplier.supplierId);
                    setEditSupplierName(supplier.name);
                    setEditContactName(supplier.contactName);
                    setEditEmail(supplier.email);
                    setEditPhone(supplier.phone);
                    setSupplierError("");
                  }}
                >
                  Edit
                </button>

                <button
                  type="button"
                  onClick={() => {
                    setDeleteSupplierId(supplier.supplierId);
                    setDeleteSupplierName(supplier.name);
                    setSupplierError("");
                  }}
                >
                  Delete
                </button>
              </>
            )}
          </div>
        ))}
      </div>




       {/* SUPPLIER DELETE MODAL */}
            {deleteSupplierId !== null && (
              <div className="delete-modal-overlay">
                <div className="delete-modal">
                  <h2>Delete Supplier</h2>

                  <p>
                    Are you sure you want to delete the supplier
                    {" "}
                    "{deleteSupplierName}"?
                  </p>

                  <div className="delete-modal-buttons">
                    <button
                      type="button"
                      onClick={() => {
                        setDeleteSupplierId(null);
                        setDeleteSupplierName("");
                        setSupplierError("");
                      }}
                    >
                      Cancel
                    </button>

                    <button
                      type="button"
                      onClick={() =>
                        handleDeleteSupplier(deleteSupplierId)
                      }
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            )}





    </section>
  );







}

export default SupplierSection;

