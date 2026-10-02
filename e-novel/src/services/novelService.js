import axios from "axios";

export const getAllNovels = async () => {

    const response = await axios.get("http://localhost:8080/novel/all");

    return response.data;
};