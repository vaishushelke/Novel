import { Link } from "react-router-dom";
import "./Navbar.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

import {
    faHouse,
    faBookOpen,
    faLayerGroup,
    faCartShopping,
    faUser
} from "@fortawesome/free-solid-svg-icons";


function Navbar() {

        const navigate = useNavigate();

    const [isLoggedIn, setIsLoggedIn] =
        useState(
            !!localStorage.getItem("token")
        );

    const handleLogout = () => {

        localStorage.removeItem("token");
        localStorage.removeItem("username");

        setIsLoggedIn(false);

        alert("Logout successful");

        navigate("/login");
    };

    return (
        <nav className="navbar">

            <div className="logo">
                <Link to="/">
                  📚 NovelNest
                </Link>
            </div>

            <div className="nav-links">

                <Link to="/">
                <FontAwesomeIcon icon={faHouse} />
                    Home
                </Link>

                <Link to="/novels">
                <FontAwesomeIcon icon={faBookOpen} />
                    Novels
                </Link>

                <Link to="/cart">
                <FontAwesomeIcon icon={faCartShopping} />
                    Cart
                </Link>
                  {!isLoggedIn ? (

                    <>
                        <Link to="/login" className="login-btn">
                         <FontAwesomeIcon icon={faUser} />
                            Login
                        </Link>

                        <Link to="/register" className="register-btn">
                          <FontAwesomeIcon icon={faUser} />
                            Register
                        </Link>
                    </>

                ) : (

                    <button
                        onClick={handleLogout}
                        className="logout-btn"
                    >
                        Logout
                    </button>

                )}

            </div>

        </nav>
    );
}

export default Navbar;