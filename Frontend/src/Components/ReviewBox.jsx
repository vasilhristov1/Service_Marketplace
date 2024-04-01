import React, { useState, useEffect } from 'react';
import { Carousel } from 'react-responsive-carousel';
import moment from 'moment';
import { getFilesByReviewId, getUserById, updateCurrentReview } from '../service/ApiService';
import '../styles/ReviewBox.css';
import { FaEdit } from "react-icons/fa";
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

const ReviewBox = ({ review, updateReviews }) => {
    const [reviewImages, setReviewImages] = useState([]);
    const reviewDate = moment(review.updatedAt, 'YYYY-MM-DD HH:mm:ss').toLocaleString();
    const [customer, setCustomer] = useState('');
    const [editMode, setEditMode] = useState(false);
    const navigate = useNavigate();
    const [newReview, setNewReview] = useState({
        description: review.description,
        rating: review.rating,
        customerId: review.customerId,
        serviceId: review.serviceId,
        isActive: review.isActive,
    });
    const localToken = localStorage.getItem('Jwt_Token');
    const decodedToken = jwtDecode(localToken);
    const userId = decodedToken['jti'];

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getFilesByReviewId(review.id);

                const imagesUrls = response.map((image) => image.url);

                setReviewImages(imagesUrls);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        const getCustomer = async () => {
            const customer = await getUserById(review.customerId);
            const customerName = customer.firstName + ' ' + customer.lastName;
            setCustomer(customerName);
        }

        getCustomer();
    }, []);

    const carouselSettings = {
        showThumbs: false,
        interval: 3000,
        infiniteLoop: true,
        autoPlay: true,
        transitionTime: 600,
        stopOnHover: false,
        dynamicHeight: false,
    };

    const handleInputChange = (e) => {
        setNewReview({ ...newReview, [e.target.name]: e.target.value });
    };

    const handleSaveReview = async (e) => {
        if (!localToken) {
            console.error('No token found');
            navigate('/login');
            return;
        }

        if (!userId) {
            console.error('No user email found');
            navigate('/login');
            return;
        }

        if (!newReview.description) {
            alert('Please add description');
            return;
        }

        if (newReview.description.length < 2) {
            alert('The description has to be more than 2 symbols');
            return;
        }

        if (!newReview.rating) {
            alert('Please add rating');
            return;
        }

        if (isNaN(parseFloat(newReview.rating)) || !isFinite(parseFloat(newReview.rating))) {
            alert('Rating has to be a valid number');
            return;
        }

        if (parseFloat(newReview.rating) < 0 || parseFloat(newReview.rating) > 5) {
            alert('Rating has to be between 0 and 5 inclusive');
            return;
        }

        const updatedReviewData = {
            description: newReview.description,
            rating: parseFloat(newReview.rating),
            customerId: userId,
            serviceId: review.serviceId,
            isActive: true
        };

        try {
            const updatedReview = await updateCurrentReview(updatedReviewData, review.id);
            setNewReview(updatedReview);
            setEditMode(false);
        } catch (error) {
            console.error('Error updating profile:', error);
        }

        updateReviews(newReview);
    };

    return (
        <div key={review.id} className="review-box-service">
            <div className="review-info">
                {(!editMode ? (
                    <>
                        <h3>Customer: {customer}</h3>
                        <p>Added on: {reviewDate}</p>
                        <div className='review-carousel'>
                            <Carousel {...carouselSettings}>
                                {reviewImages.map((imageUrl, index) => (
                                    <div key={index}>
                                        <img src={imageUrl} alt={`Photo ${index + 1}`} />
                                    </div>
                                ))}
                            </Carousel>
                        </div>
                        <p>{review.description}</p>
                        <p>Rating: {review.rating}/5</p>
                        {userId == review.customerId && (
                            <div className="button-container">
                                <button className='edit-details-button' onClick={() => setEditMode(true)}>
                                    <FaEdit />
                                </button>
                            </div>
                        )}
                    </>
                ) : (
                    <>
                        <div className="input-group">
                            <label>Description:</label>
                            <input type="text" name="description" value={newReview.description} onChange={handleInputChange} />
                        </div>
                        <div className="input-group">
                            <label>Rating:</label>
                            <input type="text" name="rating" value={newReview.rating} onChange={handleInputChange} />
                        </div>
                        <button className='save-button' onClick={handleSaveReview}>Save</button>
                    </>
                )
                )}
            </div>
        </div>
    );
};

export default ReviewBox;