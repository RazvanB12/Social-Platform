import logo from "../../logo.jpg";
import UsersTable from "./UsersTable";
import React, {useEffect} from "react";
import PhotoGallery from "./PhotoGallery";
import HeaderAdmin from "../HeaderAdmin";
import {useNavigate} from "react-router-dom";

const ReportPage = () => {
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
            <div style={{ textAlign: 'center' }}>
                <h1>Reports</h1>
                <PhotoGallery />
            </div>
        </div>

    );
};
export default ReportPage;
