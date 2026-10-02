import { useNavigate, useLocation } from "react-router-dom";
import "./Orders.css"

function Orders() {

    const navigate = useNavigate();
    const location = useLocation();

    const order = location.state?.order;

    return (
        <div className="success-container">

            <div className="success-card">

                <div className="success-icon">
                    ✓
                </div>

                <h1>Order Placed Successfully!</h1>

                <p className="success-message">
                    Thank you for shopping with NovelNest.
                    Your order has been placed successfully.
                </p>

                {order && (
                    <div className="order-details">

                        <p>
                            <strong>Order ID:</strong> #{order.id}
                        </p>

                        <p>
                            <strong>Total Amount:</strong> ₹{order.totalAmount}
                        </p>

                        <p>
                            <strong>Status:</strong> {order.status}
                        </p>

                    </div>
                )}

                <div className="success-buttons">

                    <button onClick={() => navigate("/novels")}>
                        Continue Shopping
                    </button>

                    <button
                        className="track-btn"
                        onClick={() => navigate("/orders")}
                    >
                        My Orders
                    </button>

                </div>

            </div>

        </div>
    );
}

export default Orders;