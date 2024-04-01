import React, { useState } from 'react';
import axios from 'axios';
import { loadStripe } from '@stripe/stripe-js';
import { environment } from '../environment.js';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from "jwt-decode";



const VIP = ({ service }) => {
    const navigate = useNavigate();

    const handleBecomeVIP = async () => {
        const vipPriceId = environment.vipPriceId;

        const localToken = localStorage.getItem('Jwt_Token');
        if (!localToken) {
            console.error('No token found');
            navigate('/login');
            return;
        }

        const decodedToken = jwtDecode(localToken);
        const userEmail = decodedToken['sub'];
        const userId = decodedToken['jti'];

        const checkoutData = {
            priceId: vipPriceId,
            successUrl: 'http://localhost:3000/success',
            cancelUrl: 'http://localhost:3000/cancel',
            email: userEmail,
            userId: userId,
        };

        try {
            const response = await axios.post(`http://localhost:8080/api/subscribe/vip`, checkoutData);
            const sessionId = response.data;
            const stripe = await loadStripe(environment.stripe);

            if (stripe) {
                stripe.redirectToCheckout({ sessionId });
            }
        } catch (error) {
            console.error('Error during VIP checkout:', error);
        }
    };

    return (
        <div className="service-item">
            <p>{service.title}</p>
            <p>{service.description}</p>
            <button onClick={handleBecomeVIP}>Become VIP</button>
        </div>
    );
}

export default VIP;