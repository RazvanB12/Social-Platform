import React, { useState, useEffect } from 'react';
import { Switch } from '@mui/material'; // Import Switch from Material-UI
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CancelIcon from '@mui/icons-material/Cancel';
import AccountBoxIcon from '@mui/icons-material/AccountBox';
import SkipPreviousIcon from '@mui/icons-material/SkipPrevious';
import SkipNextIcon from '@mui/icons-material/SkipNext';
import UnpublishedIcon from '@mui/icons-material/Unpublished';
import { useNavigate } from 'react-router-dom';

import './UsersTable.css';

const UsersTable = () => {
    const [users, setUsers] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [showActiveOnly, setShowActiveOnly] = useState(false);
    const navigate = useNavigate();

    const rowsPerPage = 5;

    useEffect(() => {
        async function fetchUsers() {
            try {
                const token = sessionStorage.getItem('token');
                const response = await fetch('http://localhost:8080/admin/clients', {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });
                const data = await response.json();
                setUsers(data.response);
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        }

        fetchUsers();
    }, []); // Empty dependency array ensures the effect runs only once on component mount

    const indexOfLastRow = currentPage * rowsPerPage;
    const indexOfFirstRow = indexOfLastRow - rowsPerPage;
    let filteredUsers = users.filter(user => user.email.toLowerCase().includes(searchTerm.toLowerCase()));

    if (showActiveOnly) {
        filteredUsers = filteredUsers.filter(user => !user.active);
    }

    const currentRows = filteredUsers.slice(indexOfFirstRow, indexOfLastRow);
    const totalPages = Math.ceil(filteredUsers.length / rowsPerPage);

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    const handleCancel = async (clientId) => {
        const token = sessionStorage.getItem('token');
        try {
            await fetch(`http://localhost:8080/admin/delete-client-account/${clientId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            const updatedUsers = users.filter(user => user.id !== clientId);
            setUsers(updatedUsers);
            alert("Delete done!");
        } catch (error) {
            console.error('Error deleting :', error);
            alert("Error, try again!");
        }
    };

    const handleActivate = async (clientId) => {
        const token = sessionStorage.getItem('token');
        try {
            await fetch(`http://localhost:8080/activate-client-account/${clientId}`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            const updatedUsers = users.map(user => {
                if (user.id === clientId) {
                    return { ...user, active: true };
                }
                return user;
            });
            setUsers(updatedUsers);
            alert("Validation done!");
        } catch (error) {
            console.error('Error activating :', error);
            alert("Error, try again!");
        }
    };

    const handleReject = async (clientId) => {
        const token = sessionStorage.getItem('token');
        try {
            await fetch(`http://localhost:8080/reject-client-account/${clientId}`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            const updatedUsers = users.filter(user => user.id !== clientId);
            setUsers(updatedUsers);
            alert("Validation rejected!");
        } catch (error) {
            console.error('Error rejecting :', error);
            alert("Error, try again!");
        }
    };

    return (
        <div>
            <div className="search-bar">
                <div className="switch-container">
                    <label>
                        Inactive users
                        <Switch // Replace checkbox with Switch
                            checked={showActiveOnly}
                            onChange={() => setShowActiveOnly(!showActiveOnly)}
                        />
                    </label>
                </div>
                <input
                    type="text"
                    placeholder="Search by email..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>
            <table className="users-table">
                <thead>
                <tr>
                    <th>Email</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Active</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {currentRows.map((user, index) => (
                    <tr key={index}>
                        <td className="center">{user.email}</td>
                        <td className="center">{user.firstName}</td>
                        <td className="center">{user.lastName}</td>
                        <td className="center">{user.active ? 'Yes' : 'No'}</td>
                        <td>
                            {user.active ? (
                                <>
                                    <button onClick={() => navigate(`/allcontent/${user.id}`)}>
                                        <AccountBoxIcon style={{ color: 'blue' }} />
                                    </button>
                                    <span style={{ marginRight: '10px' }}></span>
                                    <button data-testid="cancel-button" onClick={() => handleCancel(user.id)}>
                                        <CancelIcon style={{ color: 'red' }} />
                                    </button>
                                </>
                            ) : (
                                <>
                                    <button data-testid="activate-button" onClick={() => handleActivate(user.id)}>
                                        <CheckCircleIcon style={{ color: 'green' }} />
                                    </button>
                                    <span style={{ marginRight: '10px' }}></span>
                                    <button data-testid="reject-button" onClick={() => handleReject(user.id)}>
                                        <UnpublishedIcon style={{ color: 'red' }} />
                                    </button>
                                </>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <div className="pagination">
                <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1} style={{ marginRight: '10px' }}>
                    <SkipPreviousIcon />
                </button>
                <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages}>
                    <SkipNextIcon />
                </button>
            </div>
        </div>
    );
};

export default UsersTable;
