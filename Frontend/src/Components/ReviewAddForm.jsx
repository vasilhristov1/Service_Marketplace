import React from 'react';
import { createReview } from '../service/ApiService';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from "jwt-decode";
import { useState } from 'react';
import '../styles/ReviewAddForm.css';
import Rating from 'react-rating-stars-component';

const ReviewAddForm = ({ onAdd, serviceId }) => {
    const [reviewDescription, setReviewDescription] = useState('');
    const [reviewRating, setReviewRating] = useState('');
    const [selectedFiles, setSelectedFiles] = useState([]);
    const navigate = useNavigate();
    const localToken = localStorage.getItem('Jwt_Token');

    if (!localToken) {
        console.error('No JWT token found');
        navigate('/sign-in');
        return;
    }

    const decodedToken = jwtDecode(localToken);
    const userId = decodedToken['jti'];

    const handleImageChange = (event) => {
        const files = event.target.files;
        setSelectedFiles(files);
    }

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!reviewDescription) {
            alert('Please add description');
            return;
        }

        if (reviewDescription.length < 2) {
            alert('The description has to be more than 2 symbols');
            return;
        }

        if (!reviewRating) {
            alert('Please add rating');
            return;
        }

        if (isNaN(parseFloat(reviewRating)) || !isFinite(parseFloat(reviewRating))) {
            alert('Rating has to be a valid number');
            return;
        }

        if (parseFloat(reviewRating) < 0 || parseFloat(reviewRating) > 5) {
            alert('Rating has to be between 0 and 5 inclusive');
            return;
        }

        const reviewRequest = {
            description: reviewDescription,
            rating: parseFloat(reviewRating),
            customerId: userId,
            serviceId: serviceId,
            isActive: true
        };

        onAdd(reviewRequest, selectedFiles);

        setReviewDescription('');
        setReviewRating('');
        setSelectedFiles([]);
    };

    return (
        <div className='review-add-form-container'>
            <hr />
            <h2 className="review-add-form-title">Add Review</h2>
            <form className="review-add-form" onSubmit={handleSubmit}>
                <div className="form-control">
                    <label>Description:</label>
                    <input
                        type="text"
                        className="review-description-input"
                        value={reviewDescription}
                        onChange={(e) => setReviewDescription(e.target.value)}
                    />
                </div>
                <div className="form-control">
                    <label>Rating:</label>
                    <Rating
                        count={5}
                        value={reviewRating}
                        size={24}
                        onChange={(newValue) => setReviewRating(newValue)}
                        activeColor="#ffd700" 
                    />

                </div>
                <div className='form-control'>
                    <label>Pictures</label>
                    <input type="file" onChange={handleImageChange} accept="image/*" multiple />
                </div>
                <button className="submit-button" type="submit">Submit</button>
            </form>
            <hr className='bottom' />
        </div>

    );
};

export default ReviewAddForm;
