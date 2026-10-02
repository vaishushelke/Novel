import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Auth.css"

const Login = () => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate();


    const handleLogin = async (e) => {

        e.preventDefault();

        try {

            const response = await fetch(
                "http://localhost:8080/auth/login",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        username: username,
                        password: password
                    })
                }
            );


            const data = await response.json();


            if (response.ok) {

                const token =
                    data.token ||
                    data.jwt ||
                    data.accessToken;

                if (!token) {
                    console.error("Login response did not contain a token:", data);
                    alert("Login succeeded, but the server did not return an access token");
                    return;
                }

                // Store JWT
                localStorage.setItem(
                    "token",
                    token
                );


                // Store username
                localStorage.setItem(
                    "username",
                    username
                );


                alert(data.message);

                // Go to home page
                navigate("/");

            } else {

                alert(
                    data.message ||
                    "Login failed"
                );
            }

        } catch (error) {

            console.error("Login error:", error);

            alert(
                "Unable to connect to server"
            );
        }
    };


    return (

        <div className="auth-container">

            <h2>Login</h2>


            <form onSubmit={handleLogin}>

                <input
                    type="email"
                    placeholder="Enter Email"
                    value={username}
                    onChange={(e) =>
                        setUsername(e.target.value)
                    }
                    required
                />


                <input
                    type="password"
                    placeholder="Enter Password"
                    value={password}
                    onChange={(e) =>
                        setPassword(e.target.value)
                    }
                    required
                />


                <button type="submit">
                    Login
                </button>

            </form>


            <p>
                Don't have an account?
            </p>


            <button
                onClick={() =>
                    navigate("/register")
                }
            >
                Register
            </button>

        </div>
    );
};


export default Login;