import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./Auth.css";

const VerifyOTP = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const username = location.state?.username;

    const [otp, setOtp] = useState("");

    const handleVerify = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch(
                "http://localhost:8080/auth/verify-otp",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        username: username,
                        otp: otp
                    })
                }
            );

            const data = await response.json();

            if (response.ok) {
                alert(data.message);
                navigate("/login");
            } else {
                alert(data.message || "OTP verification failed");
            }

        } catch (error) {
            console.error("OTP verification error:", error);
            alert("Unable to connect to server");
        }
    };

    return (
        <div className="auth-container">

            <h2>Verify OTP</h2>

            <p>
                OTP sent to: {username}
            </p>

            <form onSubmit={handleVerify}>

                <input
                    className="otp-input"
                    type="text"
                    placeholder="000000"
                    value={otp}
                    onChange={(e) => setOtp(e.target.value)}
                    maxLength="6"
                    required
                />

                <button type="submit">
                    Verify OTP
                </button>

            </form>

        </div>
    );
};

export default VerifyOTP;