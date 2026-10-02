import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./Checkout.css"


function Checkout() {

    const navigate = useNavigate();

    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [placingOrder, setPlacingOrder] = useState(false);

    const [address, setAddress] = useState({
        name: "",
        phone: "",
        addressLine: "",
        city: "",
        state: "",
        pincode: ""
    });


    // Clear cart
    
    const clearCart = async (cartId) => {

    try {

        console.log("Clearing cart:", cartId);

        await api.post(`/cart/clear/${cartId}`);

        setCartItems([]);

        console.log("Cart cleared successfully");

    } catch (error) {

        console.error("Clear cart error:", error);

        alert(
            error.response?.data?.message ||
            "Unable to clear cart"
        );
    }
};
    // =====================================================
    // CHECK LOGIN + FETCH CART
    // ====================================================


    useEffect(() => {

        const token = localStorage.getItem("token");

        if (!token) {
            navigate("/login");
            return;
        }

        fetchCart();

    }, [navigate]);


    // =====================================================
    // GET CART
    // =====================================================

    const fetchCart = async () => {

        try {

            const response = await api.get("/cart/getC");

            console.log("Cart response:", response.data);

            setCartItems(
                Array.isArray(response.data)
                    ? response.data
                    : []
            );

        } catch (error) {

            console.error("Checkout cart error:", error);

            alert(
                error.response?.data?.message ||
                "Unable to load cart"
            );

        } finally {

            setLoading(false);
        }
    };


    // =====================================================
    // ADDRESS CHANGE
    // =====================================================

    const handleAddressChange = (e) => {

        const { name, value } = e.target;

        setAddress({
            ...address,
            [name]: value
        });
    };


    // =====================================================
    // TOTAL
    // =====================================================

    const getTotal = () => {

        return cartItems.reduce(
            (total, item) =>
                total +
                (
                    item.novel.price *
                    item.quentity
                ),
            0
        );
    };


    // =====================================================
    // VALIDATE ADDRESS
    // =====================================================

    const validateAddress = () => {

        if (!address.name.trim()) {
            alert("Please enter your name");
            return false;
        }

        if (!/^[0-9]{10}$/.test(address.phone)) {
            alert("Please enter a valid 10 digit phone number");
            return false;
        }

        if (!address.addressLine.trim()) {
            alert("Please enter your address");
            return false;
        }

        if (!address.city.trim()) {
            alert("Please enter your city");
            return false;
        }

        if (!address.state.trim()) {
            alert("Please enter your state");
            return false;
        }

        if (!/^[0-9]{6}$/.test(address.pincode)) {
            alert("Please enter a valid 6 digit pincode");
            return false;
        }

        return true;
    };


    // =====================================================
    // CREATE RAZORPAY ORDER
    // =====================================================

    const placeOrder = async () => {

        if (cartItems.length === 0) {

            alert("Your cart is empty");
            return;
        }

        if (!validateAddress()) {
            return;
        }

        try {

            setPlacingOrder(true);

            // =================================================
            // STEP 1: CREATE RAZORPAY ORDER
            // =================================================

            const response = await api.post(
                "/order/create-order"
            );

            
            console.log(
                "Razorpay order response:",
                response.data
            );

            

            const razorpayOrderId =
                response.data.id;

            const amount =
                response.data.amount;

            const currency =
                response.data.currency;

            const razorpayKey =
                response.data.key;


            // =================================================
            // STEP 2: RAZORPAY OPTIONS
            // =================================================

            const options = {

                key: razorpayKey,

                amount: amount,

                currency: currency,

                name: "NovelNest",

                description: "NovelNest Order",

                order_id: razorpayOrderId,


                // =================================================
                // STEP 3: PAYMENT SUCCESS
                // =================================================

                handler: async function (paymentResponse) {

                    try {

                        console.log(
                            "Payment response:",
                            paymentResponse
                        );

                          // Get cart ID
              const cartId = cartItems[0]?.cart?.id;

             console.log("Cart ID:", cartId);

        // Clear cart
               if (cartId) {
                    await clearCart(cartId);
                 }

            alert("Payment successful! Order placed successfully.");

            navigate("/Myorders");
                      

                    } catch (error) {

                        console.error(
                            "Payment verification error:",
                            error
                        );

                        alert(
                            error.response?.data?.message ||
                            "Payment verification failed"
                        );
                    }
                },
                
              

                // =================================================
                // CUSTOMER DETAILS
                // =================================================

                prefill: {

                    name: address.name,

                    contact: address.phone
                },


                // =================================================
                // DELIVERY DETAILS
                // =================================================

                notes: {

                    delivery_name:
                        address.name,

                    delivery_address:
                        address.addressLine,

                    delivery_city:
                        address.city,

                    delivery_state:
                        address.state,

                    delivery_pincode:
                        address.pincode
                },

               
                theme: {
                    color: "#3399cc"
                }
            };


            // =================================================
            // CHECK RAZORPAY SCRIPT
            // =================================================

            if (!window.Razorpay) {

                alert(
                    "Razorpay is not loaded."
                );

                return;
            }


            // =================================================
            // OPEN RAZORPAY
            // =================================================

            const razorpay =
                new window.Razorpay(options);


            // =================================================
            // PAYMENT FAILED
            // =================================================
          
            razorpay.on(
                "payment.failed",
                function (response) {

                    console.error(
                        "Payment failed:",
                        response.error
                    );

                    alert(
                        response.error?.description ||
                        "Payment failed"
                    );
                }
            );


            razorpay.open();

            
    

        } catch (error) {

            console.error(
                "Create order error:",
                error
            );

            alert(
                error.response?.data?.message ||
                "Unable to create Razorpay order"
            );

        } finally {

            setPlacingOrder(false);
        }

       
      };
     


    // =====================================================
    // LOADING
    // =====================================================

    if (loading) {

        return (
            <div className="checkout-loading">
                Loading checkout...
            </div>
        );
          
    }

    // =====================================================
    // UI
    // =====================================================

    return (

        <div className="checkout-page">

            <div className="checkout-container">

                <h1>Checkout</h1>

                <div className="checkout-content">


                    {/* ================================================= */}
                    {/* LEFT SIDE */}
                    {/* ================================================= */}

                    <div className="checkout-left">


                        {/* ================= ADDRESS ================= */}

                        <div className="checkout-section">

                            <h2>
                                Delivery Address
                            </h2>

                            <div className="address-form">

                                <input
                                    type="text"
                                    name="name"
                                    placeholder="Full Name"
                                    value={address.name}
                                    onChange={handleAddressChange}
                                />

                                <input
                                    type="text"
                                    name="phone"
                                    placeholder="Phone Number"
                                    maxLength="10"
                                    value={address.phone}
                                    onChange={handleAddressChange}
                                />

                                <textarea
                                    name="addressLine"
                                    placeholder="Address"
                                    value={address.addressLine}
                                    onChange={handleAddressChange}
                                />

                                <input
                                    type="text"
                                    name="city"
                                    placeholder="City"
                                    value={address.city}
                                    onChange={handleAddressChange}
                                />

                                <input
                                    type="text"
                                    name="state"
                                    placeholder="State"
                                    value={address.state}
                                    onChange={handleAddressChange}
                                />

                                <input
                                    type="text"
                                    name="pincode"
                                    placeholder="Pincode"
                                    maxLength="6"
                                    value={address.pincode}
                                    onChange={handleAddressChange}
                                />

                            </div>

                        </div>


                        {/* ================= CART ITEMS ================= */}

                        <div className="checkout-section">

                            <h2>
                                Order Summary
                            </h2>


                            {cartItems.map((item) => (

                                <div
                                    className="checkout-item"
                                    key={item.id}
                                >

                                    <img
                                        src={item.novel.imageUrl}
                                        alt={item.novel.title}
                                    />


                                    <div className="checkout-item-info">

                                        <h3>
                                            {item.novel.title}
                                        </h3>

                                        <p>
                                            Author:{" "}
                                            {item.novel.author}
                                        </p>

                                        <p>
                                            quentity:{" "}
                                            {item.quentity}
                                        </p>

                                        <p>
                                            Price: ₹
                                            {item.novel.price.toFixed(2)}
                                        </p>

                                    </div>


                                    <div className="checkout-item-price">

                                        ₹
                                        {(
                                            item.novel.price *
                                            item.quentity
                                        ).toFixed(2)}

                                    </div>

                                </div>

                            ))}

                        </div>

                    </div>


                    {/* ================================================= */}
                    {/* RIGHT SIDE */}
                    {/* ================================================= */}

                    <div className="checkout-summary">

                        <h2>
                            Order Total
                        </h2>


                        <div className="summary-row">

                            <span>
                                Items
                            </span>

                            <span>
                                {cartItems.length}
                            </span>

                        </div>


                        <div className="summary-row">

                            <span>
                                Total
                            </span>

                            <strong>
                                ₹{getTotal().toFixed(2)}
                            </strong>

                        </div>


                        {/* CHECKOUT BUTTON */}

                        <button
                            className="place-order-btn"
                            onClick={placeOrder}
                            disabled={placingOrder}
                        >

                            {placingOrder
                                ? "Processing..."
                                : "Checkout"
                            }

                        </button>


                        {/* BACK TO CART */}

                        <button
                            className="back-cart-btn"
                            onClick={() =>
                                navigate("/cart")
                            }
                        >
                            Back to Cart
                        </button>

                    </div>

                </div>

            </div>

        </div>
    );
}

export default Checkout;