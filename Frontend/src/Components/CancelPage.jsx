import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/CancelPage.css';


const CancelPage = () => {
    const navigate = useNavigate();

    const onButtonClick = () => {
        navigate("/home-page")
    }

    return (
        <div className="cancel-page-container">
            <h1>Canceled</h1>
            <p>Your payment process has been canceled.</p>
            <button onClick={onButtonClick}>Go Home</button>
        </div>
    );
};

export default CancelPage;
