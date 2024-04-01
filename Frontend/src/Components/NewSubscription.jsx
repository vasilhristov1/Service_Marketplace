import React, { useState, useEffect } from 'react';
import axios from 'axios';
import CustomerDetails from './CustomerDetails';

const NewSubscription = () => {
    const [planData, setPlanData] = useState(null);

    useEffect(() => {
        const fetchPlanData = async () => {
            try {
                const response = await axios.get(
                    'https://api.stripe.com/v1/plans/price_1OcUNzI2KDxgMJyoxeNLRi93',
                    {
                        headers: {
                            Authorization: `Bearer sk_test_51OcQX6I2KDxgMJyoLEPzCcdVgucBUKxHjaTYal5aaj0i3z4PzUCktvxT1yjiJKCmOYiqes1OKtzkTvbNWjolFjrm00Tzq3PmyY`,
                        },
                    }
                );

                console.log('Stripe API response:', response.data);

                setPlanData(response.data);
            } catch (error) {
                console.error('Error fetching plan data', error);
            }
        };

        fetchPlanData();
    }, []);

    return (<>
        <form>
            <div>
                <h1>Become Provider</h1>
                    <p>${planData.amount / 100} per {planData.interval}</p>
                <CustomerDetails data={planData} endpoint={"/api/subscribe/subscriptions/new"} />
            </div>
        </form>
    </>
    );
};

export default NewSubscription;