import React, { useEffect, useState } from 'react';
import { useParams, useLocation } from 'react-router-dom';
import { getServiceById, getCityById, getCategoryById, getUserById, getAllCities, createRequest, getFilesByServiceId, getReviewsByServiceId, createReview } from '../service/ApiService';
import RequestAddForm from './RequestAddForm';
import '../styles/ServiceDetailsPage.css';
import moment from 'moment';
import { Carousel } from 'react-responsive-carousel';
import ReviewAddForm from './ReviewAddForm';
import ReviewBox from './ReviewBox';
import { useNotification } from './NotificationProvider';
import {v4} from "uuid";

const ServiceDetailsPage = () => {
    const [showAddRequestForm, setShowAddRequestForm] = useState(false);
    const [images, setImages] = useState([]);
    const [reviews, setReviews] = useState([]);
    const [showReviews, setShowReviews] = useState(false);
    const [showAddReviewForm, setShowAddReviewForm] = useState(false);
    const [inputVal, setInputVal] = useState("");
    const dispatch = useNotification();
    const [service, setService] = useState({
        id: 0,
        title: '',
        providerId: 0,
        providerName: '',
        description: '',
        price: '',
        categoryId: '',
        categoryName: '',
        cityIds: [],
        updatedAt: []
    });
    const { serviceId } = useParams();
    const [cities, setCities] = useState([]);
    const getCitiesNames = (service, cities) => {
        const serviceCities = service.cityIds.map((cityId) => {
            const city = cities.find((city) => city.id === cityId);
            return city ? city.name : null;
        });

        return serviceCities.filter(Boolean).join(', ');
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getAllCities();

                const citiesWithLabel = response.map((city) => ({
                    id: city.id,
                    name: city.name,
                }));

                setCities(citiesWithLabel);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getFilesByServiceId(serviceId);

                const imagesUrls = response.map((image) => image.url);

                setImages(imagesUrls);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const reviews = await getReviewsByServiceId(serviceId);
                setReviews(reviews);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        async function loadServiceDetails() {
            const getServiceDetails = async () => {
                const serviceDetails = await getServiceById(serviceId);
                setService(serviceDetails);
                return serviceDetails
            };

            const localService = await getServiceDetails();

            const getProvider = async () => {
                const provider = await getUserById(localService.providerId);
                const providerName = provider.firstName + ' ' + provider.lastName;
                setService((prevService) => ({ ...prevService, providerName: providerName }))
            }

            const getCategory = async () => {
                const category = await getCategoryById(localService.categoryId);
                const categoryName = category.name;
                setService((prevService) => ({ ...prevService, categoryName: categoryName }))
            }

            getProvider();
            getCategory();
        }

        loadServiceDetails();
    }, [serviceId])

    if (!service) {
        return <div>Loading...</div>;
    }
    const formattedDate = moment(service.updatedAt, 'YYYY-MM-DD HH:mm:ss').toLocaleString();

    const carouselSettings = {
        showThumbs: false,
        interval: 3000,
        infiniteLoop: true,
        autoPlay: true,
        transitionTime: 600,
        stopOnHover: false,
        dynamicHeight: false,
    };
    const handleRequestFormToggle = () => {
        setShowAddRequestForm(!showAddRequestForm);
    }

    const handleNewNotification = () => {
        dispatch({
          type: "ADD_NOTIFICATION",
          payload: {
            id: v4(),
            message: inputVal,
            title: 'Successful'
          }
        });
      };
      

    const addRequest = async (request, isVisible) => {
        try {
            const newRequest = await createRequest(request);
            console.log(newRequest);
            setInputVal("Successfully made a request");
        } catch (error) {
            console.error(error);
        }

        setShowAddRequestForm(isVisible);
        handleNewNotification();
    };
    const handleReviewToggle = () => {
        setShowReviews(!showReviews);
    }

    const handleReviewFormToggle = async () => {
        setShowAddReviewForm(!showAddReviewForm);
    }

    const addReview = async (review, files) => {
        try {
            const newReview = await createReview(review, files);

            console.log(newReview);
            setReviews([...reviews, newReview]);
            setInputVal("Successfully added review");
        } catch (error) {
            console.error(error);
        }

        handleNewNotification();
    };


    const updateReviews = (updatedReview) => {
        const updatedReviews = reviews.map(review => {
            if (review.id === updatedReview.id) {
                return updatedReview;
            } else {
                return review;
            }
        });
        setReviews(updatedReviews);
    };

    return (
        <div className='service-details-container'>
            <h2>{service.title}</h2>
            <Carousel {...carouselSettings}>
                {images.map((imageUrl, index) => (
                    <div key={index}>
                        <img src={imageUrl} alt={`Photo ${index + 1}`} />
                    </div>
                ))}
            </Carousel>
            <h3>{service.price} BGN</h3>
            <hr />
            <p>{service.categoryName}</p>
            <hr />
            <h3>Description</h3>
            <p>{service.description}</p>
            <hr />
            <p>Provider: {service.providerName}</p>
            <hr />
            <h3>Location</h3>
            <p>{getCitiesNames(service, cities)} </p>
            <hr />
            <p>Added on: {formattedDate}</p>

            <button className='pay-button' onClick={handleRequestFormToggle}>Make a request</button>
            {
                showAddRequestForm && (
                    <RequestAddForm onAdd={addRequest} serviceId={serviceId} />
                )
            }
            <button className='add-review-btn' onClick={handleReviewFormToggle} >Add review</button>

            <button className='reviews-btn' onClick={handleReviewToggle}>Reviews</button>
            {showAddReviewForm && (
                <ReviewAddForm onAdd={addReview} serviceId={serviceId} />
            )}
            {showReviews && (
                <div className='reviews-container'>
                    {reviews.length > 0 ? (
                        reviews.map((review, index) => (
                            <ReviewBox key={index} review={review} updateReviews={updateReviews} />
                        ))
                    ) : (
                        'No reviews to show'
                    )}
                </div>
            )}

        </div >
    );
};
export default ServiceDetailsPage;