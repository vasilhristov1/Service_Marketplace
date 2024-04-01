import React from 'react'
import AddService from './AddService'
import { useState, useEffect } from 'react';
import '../styles/AddServicePage.css'
import { getAllServices, createService } from '../service/ApiService';
import { useNotification } from './NotificationProvider';
import {v4} from "uuid";

function AddServicePage() {
    const [services, setServices] = useState([]);
    const [inputVal, setInputVal] = useState("");
    const dispatch = useNotification();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const services = await getAllServices();
                setServices(services);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

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

    const addService = async (service, files) => {
        try {
            const newService = await createService(service, files);

            setServices([...services, newService]);
            setInputVal("Successfully added new service");
        } catch (error) {
            console.error(error);
        }

        handleNewNotification();
    };

    return (
        <div className='AddServicePage'>
            <AddService onAdd={addService} />
        </div>
    )
}

export default AddServicePage;
