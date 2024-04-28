import React, { useState, useEffect } from 'react';
import UsersTable from './UsersTable';
import HeaderAdmin from "../HeaderAdmin";
import {useNavigate} from "react-router-dom";

const AdminPage = () => {
    const [users, setUsers] = useState([]);
    const navigate = useNavigate();
    useEffect(() => {

        const hasAccessedBefore =
            sessionStorage.getItem('accessedBefore') === 'true';

        if (!hasAccessedBefore) {
            navigate('/login');
            sessionStorage.setItem('accessedBefore', 'true');
        }

        const token = sessionStorage.getItem('token');
        if (!token) {
            navigate('/login');
        }

    }, [navigate]);
    return (
        <div>
            <HeaderAdmin/>
            <div style={styles.container}>
                <div style={styles.content}>
                    <h2>Manage User Accounts</h2>
                    <UsersTable  />
                </div>
            </div>
        </div>
    );
};

export default AdminPage;

const styles = {
    container: {
        maxWidth: '1000px',
        margin: 'auto',
        padding: '20px',
        textAlign: 'center',
        marginTop: '50px'
    },
    header: {
        marginBottom: '20px',
    },
    logo: {
        width: '80px',
        height: '80px',
        marginBottom: '10px',
    },
    heading: {
        fontSize: '24px',
        fontWeight: 'bold',
        margin: '0',
    },
    content: {
        backgroundColor: '#f9f9f9',
        padding: '20px',
        borderRadius: '8px',
    },
};
