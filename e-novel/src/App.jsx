import { BrowserRouter, Routes, Route } from "react-router-dom";

import Navbar from "./components/Navbar";
import Home from "./components/Home";
import FrontPage from "./components/FrontPage";


import Login from "./pages/Login";
import Register from "./pages/Register";

import Cart from "./pages/Cart";
import Verifyotp from "./pages/Verifyotp";
import Checkout from "./pages/Checkout";
import Orders from "./pages/MyOrders";


function App() {
    return (
        <BrowserRouter>

            <Navbar />

            <Routes>

                <Route path="/" element={<FrontPage/>} />

                <Route path="/login" element={<Login />} />

                <Route path="/register" element={<Register />} />
                
                <Route path="/verify-otp" element={<Verifyotp />} />

                <Route path="/novels" element={<Home />} />

                <Route path="/cart" element={<Cart />} />

                <Route path="/checkout" element={<Checkout />} />

                <Route path="/MyOrders" element={<Orders />} />

                {/* <Route path="*" element={<NotFound />} /> */}

            </Routes>

        </BrowserRouter>
    );
}

export default App;