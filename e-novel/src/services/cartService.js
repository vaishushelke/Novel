import api from "./api";

export const addNovelToCart = (novelId) => {
    return api.post(`/cart/add/${novelId}`);
};

export const getApiErrorMessage = (error, fallbackMessage) => {
    const responseData = error.response?.data;

    if (typeof responseData === "string") {
        return responseData;
    }

    return responseData?.message || error.message || fallbackMessage;
};