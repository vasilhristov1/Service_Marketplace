import React, {useState, useEffect} from 'react';
import axios from 'axios';
import '../styles/Subscription.css';

const Subscription = ({ priceId, onSelected, isSelected }) => {
    const [productData, setProductData] = useState();
    const [params] = useState({
        priceId: priceId
    });

    useEffect(() => {
        const fetchSubscriptionProductData = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/subscribe/plan`, {params: params
                });
                setProductData(response.data);
            } catch (error) {
                console.error('Error fetching subscription product data:', error);
            }
        };

        fetchSubscriptionProductData();
    }, []);

    const handleClick = () => {
        onSelected(priceId);
    };

    function getCurrencySymbol(currencyCode) {
        switch (currencyCode) {
            case 'usd':
                return '$';
            case 'eur':
                return '€';
            case 'gbp':
                return '£';
            case 'jpy':
                return '¥';
            default:
                return currencyCode;
        }
    }

  return (
    <div className={`subscription-box ${isSelected ? 'selected' : ''}`} onClick={handleClick} style={{ cursor: 'pointer' }}>
        {productData ? (
                <div className='subscription-info'>
                    <h3 className='subscription-name'>{productData.recurring.interval === 'month' ? (productData.recurring.interval_count === 1 ? 'Monthly' : 'Half Year') : 'Yearly'} Subscription</h3>
                    <p className='subscription-details'>{getCurrencySymbol(productData.currency)}{productData.unit_amount / 100} per {productData.recurring.interval_count} {productData.recurring.interval}{productData.recurring.interval_count > 1 ? 's' : ''}</p>
                </div>
            ) : (
                <p className='loading'>Loading...</p>
            )}
    </div>
  )
}

export default Subscription;
