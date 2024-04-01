import {useState} from "react";

function CustomerDetails({ data, endpoint}) {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");

    const onCustomerNameChange = (e) => {
        setName(e.target.value)
    };

    const onCustomerEmailChange = (e) => {
        setEmail(e.target.value)
    }

    const initiatePayment = () => {
        fetch('http://localhost:5173' + endpoint, {
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                item: {name: data.name, id: data.id},
                customerName: name,
                customerEmail: email,
            })
        })
            .then(r => r.text())
            .then(r => {
                window.location.href = r
            })

    }

    return <>
        <div>
            <input variant='filled' placeholder='Customer Name' onChange={onCustomerNameChange} value={name}/>
            <input variant='filled' placeholder='Customer Email' onChange={onCustomerEmailChange} value={email}/>
            <button onClick={initiatePayment} colorScheme={'green'}>Checkout</button>
        </div>
    </>
}

export default CustomerDetails