import React from 'react'
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import { useState } from 'react';
import { getRequestById } from '../service/ApiService';

export const OfferAddForm = ({ onAdd, requestId }) => {

    const [offerDescription, setOfferDescription] = useState('');
    const [offerPrice, setOfferPrice] = useState('');
    const navigate = useNavigate();
    const [request, setRequest] = useState({
        id: '',
        customerId: '',
        serviceId: '',
        description: '',
        requestStatus: ''
    }
    );
    console.log(requestId)
    const localToken = localStorage.getItem('Jwt_Token');


    useEffect(() => {
        const fetchCurrentRequest = async () => {
            try {
                const requestDetails = await getRequestById(requestId);
                setRequest(requestDetails);

            } catch (error) {
                console.error('Error fetching current user:', error);
            }
        }
        fetchCurrentRequest();
    }, []);


    if (!localToken) {
        console.error('No JWT token found');
        navigate('/sign-in');
        return;
    }
    const decodedToken = jwtDecode(localToken);
    const userId = decodedToken['jti'];






    const handleSubmit = (e) => {
        e.preventDefault();
        if (!offerDescription) {
            alert('Please add description');
            return;
        }
        if (offerDescription.length < 2) {
            alert('The description has to be more than 2 symbols');
            return;
        }
        if (!offerPrice) {
            alert('Please add price');
            return;
        }
        if (isNaN(parseFloat(offerPrice)) || !isFinite(parseFloat(offerPrice))) {
            alert('Price has to be a valid number');
            return;
        }



        const offerRequest = {
            request_id: requestId,
            provider_id: userId,
            description: offerDescription,
            price: offerPrice,
            offerStatus: 'PENDING'
        };

        const acceptRequest = {
            customerId: request.customerId,
            serviceId: request.serviceId,
            description: request.description,
            requestStatus: 'ACCEPTED'
        };

        console.log(offerRequest);

        onAdd(offerRequest,acceptRequest);
        setOfferDescription('');
        setOfferPrice('');
    };
    return (
        <div className='offer-add-form-container'>
            <hr />
            <h2 className="offer-add-form-title">Add Offer</h2>
            <form className="offer-add-form" onSubmit={handleSubmit}>
                <div className="form-control">
                    <label>Description:</label>
                    <input
                        type="text"
                        className="offer-description-input"
                        value={offerDescription}
                        onChange={(e) => setOfferDescription(e.target.value)}
                    />
                </div>
                <div className="form-control">
                    <label>Price:</label>
                    <input
                        type="text"
                        className="offer-price-input"
                        value={offerPrice}
                        onChange={(e) => setOfferPrice(e.target.value)}
                    />
                </div>
                <button className="submit-button" type="submit">Submit</button>
            </form>
            <hr className='bottom' />
        </div>
    )
}







