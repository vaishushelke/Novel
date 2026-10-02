
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Auth.css";

const Register = () => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("USER");

    const navigate = useNavigate();


    const handleRegister = async (e) => {

        e.preventDefault();

        try {

            const response = await fetch(
                "http://localhost:8080/auth/reg",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        username: username,
                        password: password,
                        role: role
                    })
                }
            );


            const data = await response.json();


            if (response.ok) {

                alert(data.message);

                navigate("/verify-otp", {
                    state: {
                        username: username
                    }
                });

            } else {

                alert(
                    data.message ||
                    "Registration failed"
                );
            }

        } catch (error) {

            console.error("Registration error:", error);

            alert(
                "Unable to connect to server"
            );
        }
    };


    return (

        <div className="auth-container">

            <h2>Create Account</h2>


            <form onSubmit={handleRegister}>

                {/* Email */}

                <input
                    type="email"
                    placeholder="Enter Email"
                    value={username}
                    onChange={(e) =>
                        setUsername(e.target.value)
                    }
                    required
                />


                {/* Password */}

                <input
                    type="password"
                    placeholder="Enter Password"
                    value={password}
                    onChange={(e) =>
                        setPassword(e.target.value)
                    }
                    required
                />


                {/* Role */}

                <select
                    value={role}
                    onChange={(e) =>
                        setRole(e.target.value)
                    }
                >

                    <option value="USER">
                        User
                    </option>

                    <option value="ADMIN">
                        Admin
                    </option>

                </select>


                <button type="submit">
                    Register
                </button>

            </form>

        </div>
    );
};


export default Register;
