import { useEffect, useState } from "react";
import { getAllNovels } from "../services/novelService";
import './Home.css';
import { addNovelToCart, getApiErrorMessage } from "../services/cartService";
import { useSearchParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";

function Home() {
const [novels, setNovels] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [searchParams] = useSearchParams();

    const navigate = useNavigate();

    useEffect(() => {
        fetchNovels();
    }, []);

    const fetchNovels = async () => {

        try {

            setLoading(true);

            const response = await getAllNovels();

            console.log("API Response:", response);

            if (response.success) {
                setNovels(response.data);
            } else {
                setError(response.message);
            }

        } catch (error) {

            console.error("Error:", error);

            if (error.response) {
                setError(
                    error.response.data?.message ||
                    "Failed to fetch novels"
                );
            } else {
                setError("Unable to connect to server");
            }

        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="loading">
                <h2>Loading novels...</h2>
            </div>
        );
    }

    if (error) {
        return (
            <div className="error">
                <h2>{error}</h2>

                <button onClick={fetchNovels}>
                    Try Again
                </button>
            </div>
        );
    }
    const addToCart = async (novelId) => {
        const token = localStorage.getItem("token");

        if (!token) {
            alert("Please login to add items to cart");
            navigate("/login");
            return;
        }

        try {
            const response = await addNovelToCart(novelId);
            const message = response.data?.message || response.data;

            alert(message || "Added to cart successfully");
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
//-----------------------------------------


const search =
    searchParams.get("search") || "";

const category =
    searchParams.get("category") || "";

    const filteredNovels = novels.filter((novel) => {

    const searchText = search.toLowerCase();

    const matchesSearch =
        novel.title?.toLowerCase().includes(searchText) ||
        novel.author?.toLowerCase().includes(searchText);

    const matchesCategory =
        category === "" ||
        novel.category === category;

    return matchesSearch && matchesCategory;
});




//----------------------------------------------------------------------------




    return (




        <div className="home">

              <section className="home-filter">

    <div className="filter-content">

        <p>FIND YOUR NEXT READ</p>

        <h2>Explore Novels</h2>

        <div className="filter-row">

            {/* Search */}
            <input
                type="text"
                placeholder="Search by title or author..."
                id="searchInput"
            />

            {/* Category */}
            <select
                id="categoryFilter"
                defaultValue=""
            >
                <option value="">
                    All Categories
                </option>

                <option value="Fiction">
                    Fiction
                </option>

                <option value="Romance">
                    Romance
                </option>

                <option value="Mystery">
                    Mystery
                </option>

                <option value="Thriller">
                    Thriller
                </option>

                <option value="Fantasy">
                    Fantasy
                </option>

                <option value="Self Help">
                    Self Help
                </option>

            </select>

            <button
                onClick={() => {

                    const search =
                        document.getElementById("searchInput").value;

                    const category =
                        document.getElementById("categoryFilter").value;

                    const params = new URLSearchParams();

                    if (search) {
                        params.append("search", search);
                    }

                    if (category) {
                        params.append("category", category);
                    }

                    navigate(`/novels?${params.toString()}`);

                }}
            >
                Search
            </button>

        </div>

    </div>

</section>
            {/* Novel Section */}


            <section className="novel-section">

                <div className="novel-grid">

                    {/* {novels.map((novel) => ( */}
                        {filteredNovels.map((novel) => (
                        <div
                            className="novel-card"
                            key={novel.id}
                        >

                            <img
                                src={novel.imageUrl}
                                alt={novel.title}
                            />

                            <div className="novel-details">

                                <h3>
                                    {novel.title}
                                </h3>

                                <p className="author">
                                    By {novel.author}
                                </p>

                                <span className="category">
                                    {novel.category}
                                </span>

                                <p className="description">
                                    {novel.description}
                                </p>

                                <div className="novel-bottom">

                                    <span className="price">
                                        ₹{novel.price}
                                    </span>

                                    <span className="stock">
                                        {novel.stock > 0
                                            ? ` ${novel.stock} in stock`
                                            : "Out of stock"

                                            }

                                    </span>

                                </div>


                                <button onClick={() => addToCart(novel.id)}
                                    className="cart-btn"
                                    disabled={novel.stock <= 0}
                                    >
                             Add to Cart
                               </button>

                            </div>

                        </div>

                    ))}

                </div>

            </section>

        </div>
    );

}

export default Home;