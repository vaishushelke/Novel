import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import { addNovelToCart, getApiErrorMessage } from "../services/cartService";
import "./Frontpage.css";

function FrontPage() {
    const [novels, setNovels] = useState([]);
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

    useEffect(() => {
        fetchNovels();
    }, []);

    const fetchNovels = async () => {
        try {
            const response = await api.get("/novel/getAll");
            console.log("Novels:", response.data);
            setNovels(response.data);
        } catch (error) {
            console.error("Error fetching novels:", error);
        } finally {
            setLoading(false);
        }
    };

    const addToCart = async (novelId) => {
        const token = localStorage.getItem("token");

        if (!token) {
            alert("Please login to add items to cart");
            navigate("/login");
            return;
        }

        try {
            const response = await addNovelToCart(novelId);
            alert(
                response.data?.message ||
                response.data ||
                "Added to cart successfully"
            );
        } catch (error) {
            console.error("Add to cart error:", error);

            if (error.response?.status === 401 || error.response?.status === 403) {
                localStorage.removeItem("token");
                navigate("/login");
            } else {
                alert(getApiErrorMessage(error, "Failed to add novel to cart"));
            }
        }
    };

    const viewNovel = (id) => {
        navigate(`/novel/${id}`);
    };

    const categories = [
        "Fiction",
        "Romance",
        "Mystery",
        "Thriller",
        "Fantasy",
        "Self Help"
    ];

    return (
        <div className="home">

            {/* ================= HERO ================= */}
            <section className="hero">
                <div className="hero-content">
                    <p className="hero-small">
                        WELCOME TO NOVELNEST
                    </p>

                    <h1>
                        Discover Your Next
                        <span> Great Story</span>
                    </h1>

                    <p className="hero-description">
                        Explore thousands of captivating novels,
                        inspiring stories and unforgettable characters.
                    </p>

                    <div className="hero-buttons">
                        <button
                            className="primary-btn"
                            onClick={() => navigate("/novels")}
                        >
                            Explore Novels
                        </button>

                        <button
                            className="secondary-btn"
                            onClick={() => navigate("/cart")}
                        >
                            View Cart
                        </button>
                    </div>
                </div>

                <div className="hero-book">
                    <div className="book">
                        <div className="book-cover">
                            <span>NovelNest</span>
                            <strong>
                                READ.<br />
                                DREAM.<br />
                                DISCOVER.
                            </strong>
                        </div>
                    </div>
                </div>
            </section>

            {/* ================= CATEGORIES ================= */}
            <section className="categories-section">
                <div className="section-heading">
                    <p>EXPLORE</p>
                    <h2>Browse by Category</h2>
                </div>

                <div className="categories">
                    {categories.map((category) => (
                        <div
                            className="category-card"
                            key={category}
                            onClick={() =>
                                navigate(
                                    `/novels?category=${encodeURIComponent(category)}`
                                )
                            }
                        >
                            <div className="category-icon">
                                📚
                            </div>

                            <h3>{category}</h3>

                            <span>
                                Explore →
                            </span>
                        </div>
                    ))}
                </div>
            </section>

            {/* ================= FEATURED NOVELS ================= */}
            <section className="featured-section">
                <div className="section-heading featured-heading">
                    <div>
                        <p>OUR COLLECTION</p>
                        <h2>Featured Novels</h2>
                    </div>

                    <button
                        className="view-all-btn"
                        onClick={() => navigate("/novels")}
                    >
                        View All →
                    </button>
                </div>

                {loading ? (
                    <div className="loading">
                        Loading novels...
                    </div>
                ) : novels.length === 0 ? (
                    <div className="no-novels">
                        No novels available.
                    </div>
                ) : (
                    <div className="novel-grid">
                        {novels.slice(0, 8).map((novel) => (
                            <div
                                className="novel-card"
                                key={novel.id}
                            >
                                <div
                                    className="novel-image"
                                    onClick={() =>
                                        viewNovel(novel.id)
                                    }
                                >
                                    <img
                                        src={novel.imageUrl}
                                        alt={novel.title}
                                    />

                                    {novel.stock <= 0 && (
                                        <span className="out-stock">
                                            Out of Stock
                                        </span>
                                    )}
                                </div>

                                <div className="novel-info">
                                    <p className="novel-category">
                                        {novel.category}
                                    </p>

                                    <h3>
                                        {novel.title}
                                    </h3>

                                    <p className="novel-author">
                                        by {novel.author}
                                    </p>

                                    <div className="novel-bottom">
                                        <span className="price">
                                            ₹{novel.price}
                                        </span>

                                        <button
                                            className="add-cart-btn"
                                            disabled={novel.stock <= 0}
                                            onClick={() =>
                                                addToCart(novel.id)
                                            }
                                        >
                                            {novel.stock <= 0
                                                ? "Unavailable"
                                                : "Add to Cart"}
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </section>

            {/* ================= PROMO ================= */}
            <section className="promo-section">
                <div className="promo-content">
                    <p>YOUR NEXT ADVENTURE AWAITS</p>

                    <h2>
                        Every Book Opens
                        <br />
                        A New World
                    </h2>

                    <p>
                        Find stories that inspire, entertain
                        and stay with you forever.
                    </p>

                    <button
                        onClick={() => navigate("/novels")}
                    >
                        Start Exploring
                    </button>
                </div>
            </section>

            {/* ================= WHY NOVELNEST ================= */}
            <section className="features-section">
                <div className="section-heading">
                    <p>WHY NOVELNEST</p>

                    <h2>
                        Your Perfect Reading Destination
                    </h2>
                </div>

                <div className="features">

                    <div className="feature">
                        <div className="feature-icon">
                            📖
                        </div>

                        <h3>
                            Huge Collection
                        </h3>

                        <p>
                            Discover novels across different
                            genres and categories.
                        </p>
                    </div>

                    <div className="feature">
                        <div className="feature-icon">
                            🛒
                        </div>

                        <h3>
                            Easy Shopping
                        </h3>

                        <p>
                            Add your favorite books to your cart
                            and checkout easily.
                        </p>
                    </div>

                    <div className="feature">
                        <div className="feature-icon">
                            🔐
                        </div>

                        <h3>
                            Secure Account
                        </h3>

                        <p>
                            Your account and orders are protected
                            with secure authentication.
                        </p>
                    </div>

                    <div className="feature">
                        <div className="feature-icon">
                            🚚
                        </div>

                        <h3>
                            Fast Delivery
                        </h3>

                        <p>
                            Get your favorite novels delivered
                            right to your doorstep.
                        </p>
                    </div>

                </div>
            </section>

        </div>
    );
}

export default FrontPage;