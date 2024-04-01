import React from 'react';
import ServiceBox from './ServiceBox';

const ServiceBoxes = ({ services, cities, handleViewMoreClick }) => {
    return (
        <>
            {services.map((service) => (
                <ServiceBox key={service.id} service={service} cities={cities} handleViewMoreClick={handleViewMoreClick}/>
            ))}
        </>
    )
}

export default ServiceBoxes;
