import logo from "../../logo.jpg";
import UsersTable from "./UsersTable";
import React, {useEffect} from "react";
import PhotoGallery from "./PhotoGallery";
import HeaderAdmin from "../HeaderAdmin";
import {useNavigate} from "react-router-dom";
import PhotoGalleryUser from "./PhotoGalleryUser";
import { useParams } from 'react-router-dom';

const UserContent = () => {
    const navigate = useNavigate();
    const { idUser } = useParams();

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
                <h1> User content </h1>
                <h1> User content </h1>
                <PhotoGalleryUser userId={idUser} />
            </div>
        </div>

    );
};
export default UserContent;
