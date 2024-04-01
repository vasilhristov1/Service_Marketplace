import React, { useState, useEffect } from 'react';
import '../styles/ServiceBox.css';
import { getAllCities, getFilesByServiceId } from '../service/ApiService';
import moment from 'moment';
import { Link } from 'react-router-dom';
import { Carousel } from 'react-responsive-carousel';

const ServiceBox = ({ service, cities }) => {
    const formattedDate = moment(service.updatedAt, 'YYYY-MM-DD HH:mm:ss').toLocaleString();
    const [images, setImages] = useState([]);

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
                const response = await getFilesByServiceId(service.id);

                const imagesUrls = response.map((image) => image.url);

                setImages(imagesUrls);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
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


    return (
        <div className="service-box">
            <div className='photo'>
                {images.length > 0 ? (
                    <Carousel {...carouselSettings}>
                        {images.map((imageUrl, index) => (
                            <div key={index}>
                                <img src={imageUrl} alt={`Photo ${index + 1}`} />
                            </div>
                        ))}
                    </Carousel>
                ) : (
                    <img src="https://www.shutterstock.com/image-vector/service-tool-icon-this-isolated-260nw-274711127.jpg"></img>
                )
                }
            </div>
            <div className='service-info'>
                <div className="service-title">
                    <h3>{service.title}</h3>
                </div>
                <div className="service-price">
                    <p>Price: {service.price} BGN.</p>
                </div>
                <div className="service-description">
                    <p>{service.description}</p>
                </div>
                <div className="service-is-vip">
                    <p>{service.vip ? "VIP" : ""}</p>
                </div>
                <div className="service-details">
                    <p>{getCitiesNames(service, cities)} - {formattedDate}</p>
                    <Link to={{
                        pathname: `service-details/${service.id}`
                    }}
                        className="view-more-btn">View More</Link>
                </div>
            </div>
        </div>
    )
}

export default ServiceBox;
