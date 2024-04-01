import React from 'react';
import { useLocation } from 'react-router-dom';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/SuccessPage.css';
import { jwtDecode } from 'jwt-decode';
import { getUserById, refreshToken } from '../service/ApiService';

const SuccessPage = () => {
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const queryParams = new URLSearchParams(location.search);
        const sessionId = queryParams.get('session_id');
        console.log('Subscription success! Session ID:', sessionId);
    }, [location.search]);

    const onButtonClick = () => {
        const handleCheckoutSuccess = async () => {
            try {
                const localToken = localStorage.getItem('Jwt_Token');
                if (!localToken) {
                    console.error('No token found');
                    navigate('/login');
                    return;
                }
    
                const decodedToken = jwtDecode(localToken);
                const userId = decodedToken['jti'];
    
                const response = await getUserById(userId);
                const updatedUserData = response;
    
                const newTokenResponse = await refreshToken(updatedUserData);
                const newToken = newTokenResponse;
    
                localStorage.setItem('Jwt_Token', newToken);
            } catch (error) {
                console.error('Error updating token:', error);
            }
        };

        handleCheckoutSuccess();
        
        navigate("/home-page");
    }

    return (
        <div className="success-page-container"> 
            <h1>Successful!</h1>
            <p>Thank you for using our services.</p>
            <button onClick={onButtonClick}>Go Home</button>
        </div>
    )
};

export default SuccessPage;