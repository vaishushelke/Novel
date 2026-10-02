import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./Cart.css"

function Cart() {

    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

    // ============================
    // FETCH CART
    // ============================
    useEffect(() => {

        const token = localStorage.getItem("token");

        if (!token) {
            navigate("/register");
            return;
        }

        fetchCart();

    }, []);

    const fetchCart = async () => {

        try {

            const response = await api.get("/cart/getC");

            console.log("Cart response:", response.data);

            console.log(
                "Full cart JSON:",
                JSON.stringify(response.data, null, 2)
            );
                setCartItems(response.data);

        } catch (error) {

            console.error("Cart error:", error);

            if (
                error.response?.status === 401 ||
                error.response?.status === 403
            ) {

                localStorage.removeItem("token");
                localStorage.removeItem("role");
                localStorage.removeItem("username");

                navigate("/login");

            } else {

                alert(
                    error.response?.data?.message ||
                    error.response?.data ||
                    "Unable to fetch cart"
                );
            }

        } finally {

            setLoading(false);
        }
    };


    // ============================
    // REMOVE ITEM
    // ============================
    const removeFromCart = async (cartItemId) => {

        try {

            const response = await api.delete(
                `/cart/item/${cartItemId}`
            );

            alert(
                response.data?.message ||
                response.data ||
                "Item removed from cart"
            );

            fetchCart();

        } catch (error) {

            console.error(
                "Remove cart error:",
                error
            );

            alert(
                error.response?.data?.message ||
                error.response?.data ||
                "Unable to remove item"
            );
        }
    };


    // ============================
    // INCREASE quentity
    // ============================
    const increasequentity = async (cartItemId) => {

        try {

            await api.put(
                `/cart/increase/${cartItemId}`
            );

            fetchCart();

        } catch (error) {

            console.error(
                "Increase quentity error:",
                error
            );

            alert(
                error.response?.data?.message ||
                error.response?.data ||
                "Unable to increase quentity"
            );
        }
    };


    // ============================
    // DECREASE quentity
    // ============================
    const decreasequentity = async (cartItemId) => {

        try {

            await api.put(
                `/cart/decrease/${cartItemId}`
            );

            fetchCart();

        } catch (error) {

            console.error(
                "Decrease quentity error:",
                error
            );

            alert(
                error.response?.data?.message ||
                error.response?.data ||
                "Unable to decrease quentity"
            );
        }
    };


    // ============================
    // CALCULATE TOTAL
    // ============================
    const getTotal = () => {

        return cartItems.reduce(
            (total, item) => {

                if (!item.novel) {
                    return total;
                }

                return total +
                    (item.novel.price * item.quentity);

            },
            0
        );
    };


    // ============================
    // LOADING
    // ============================
    if (loading) {

        return (
            <div className="cart-container">

                <h2>Loading cart...</h2>

            </div>
        );
    }


    // ============================
    // CART UI
    // ============================
    return (

        <div className="cart-container">

            <h2>🛒 My Cart</h2>


            {/* EMPTY CART */}
            {cartItems.length === 0 ? (

                <div className="empty-cart">

                    <h3>
                        Your cart is empty
                    </h3>

                    <button
                        onClick={() => navigate("/novels")}
                    >
                        Browse Novels
                    </button>

                </div>

            ) : (

                <>

                    {/* CART ITEMS */}

                    <div className="cart-items">

                        {cartItems.map((item) => {

                            // Prevent page crash if novel is missing
                            if (!item.novel) {

                                return (
                                    <div
                                        className="cart-item"
                                        key={item.id}
                                    >

                                        <div className="cart-details">

                                            <h3>
                                                Novel data not available
                                            </h3>

                                            <p>
                                                Cart Item ID: {item.id}<br/><br/>
                                                 {item.quentity}
                                                 
                                            </p>

                                            <button
                                                className="remove-btn"
                                                onClick={() =>
                                                    removeFromCart(
                                                        item.id
                                                    )
                                                }
                                            >
                                                Remove
                                            </button>

                                        </div>

                                    </div>
                                );
                            }


                            return (

                                <div
                                    className="cart-item"
                                    key={item.id}
                                  
                                >

                                    {/* NOVEL IMAGE */}
                                    
                                    <img
                                        src={item.novel.imageUrl}
                                        alt={item.novel.title}
                                    />


                                    {/* NOVEL DETAILS */}

                                    <div className="cart-details">
                            
                                        <h3>
                                            {item.novel.title}
                                        </h3>


                                        <p>
                                            Author:{" "}
                                            {item.novel.author}
                                        </p>


                                        <p>
                                            Category:{" "}
                                            {item.novel.category}
                                        </p>


                                        <p>
                                            Price: ₹
                                            {item.novel.price}
                                        </p>


                                        {/* quentity */}

                                        <div className="quentity-control">

                                            <button
                                                className="quentity-btn"
                                                onClick={() =>
                                                    decreasequentity(
                                                        item.id
                                                    )
                                                }
                                            >
                                                −
                                            </button>


                                            <span className="quentity">
                                                {item.quentity}
                                            </span>


                                            <button
                                                className="quentity-btn"
                                                onClick={() =>
                                                    increasequentity(
                                                        item.id
                                                    )
                                                }
                                            >
                                                +
                                            </button>

                                        </div>


                                        {/* SUBTOTAL */}

                                        <p>

                                            Subtotal: ₹
                                            {(
                                                item.novel.price *
                                                item.quentity
                                            ).toFixed(2)}

                                        </p>


                                        {/* REMOVE */}

                                        <button
                                            className="remove-btn"
                                            onClick={() =>
                                                removeFromCart(
                                                    item.id
                                                )
                                            }
                                        >
                                            Remove
                                        </button>

                                    </div>

                                </div>
                            );

                        })}

                    </div>


                    {/* CART SUMMARY */}

                    <div className="cart-summary">

                        <h2>
                            Total: ₹
                            {getTotal().toFixed(2)}
                        </h2>


                        <button
                            className="checkout-btn"
                            onClick={() =>
                                navigate("/checkout")
                            }
                        >
                            Proceed to Checkout
                        </button>

                    </div>

                </>

            )}

        </div>
    );
}

export default Cart;