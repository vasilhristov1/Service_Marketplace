import React, { useState, useEffect } from 'react';
import '../styles/Modal.css';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import SubscriptionComponent from './SubscriptionComponent';

function Modal({ setOpenModal }) {
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    return (
        <div className="modalBackground">
            <div className="modalContainer">
                <div className="titleCloseBtn">
                    <button onClick={() => setOpenModal(false)}>X</button>
                </div>
                <div className="title">
                    <h1>Become Provider</h1>
                </div>
                <div className="body">
                    {loading ? (
                        <p>Loading subscriptions...</p>
                    ) : (
                        <>
                            <div className="footer">
                                <SubscriptionComponent />
                            </div>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Modal;
