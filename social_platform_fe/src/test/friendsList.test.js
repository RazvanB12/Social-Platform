import React, {useState} from 'react';
import { render, fireEvent, waitFor,screen } from '@testing-library/react';
import axios from 'axios';
import { MemoryRouter } from 'react-router-dom';
import FriendsList from '../components/FriendsList';
import {act} from "react-dom/test-utils";


jest.mock('axios');


describe('FriendsList component', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });

    it('should render without crashing', () => {
        const { getByText } = render(
            <MemoryRouter>
                <FriendsList />
            </MemoryRouter>
        );

        expect(getByText('Connect')).toBeInTheDocument();
    });

    it('should fetch friend request list on component mount', async () => {
        axios.get.mockResolvedValueOnce({
            data: {
                response: [],
            },
        });

        render(
            <MemoryRouter>
                <FriendsList />
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/friend-requests', {
                headers: {
                    Authorization: `Bearer ${sessionStorage.getItem('token')}`,
                    Accept: 'application/json',
                },
            });
        });
    });

    it('should add friend when add button is clicked', async () => {
        const friendId = '123';

        axios.post.mockResolvedValueOnce({
            status: 200,
        });

        axios.get.mockResolvedValueOnce({
            data: {
                response: [
                    {
                        userId: friendId,
                        name: 'Test User',
                        friendRequestStatus: 'NOT_FRIEND',
                    },
                ],
            },
        });

        render(
            <MemoryRouter>
                <FriendsList />
            </MemoryRouter>
        );

        console.log(screen.debug());

        const addButton = await screen.findByTestId('add-button');

        console.log(addButton);

        fireEvent.click(addButton);

        await waitFor(() => {
            expect(axios.post).toHaveBeenCalledTimes(1);
            expect(axios.post).toHaveBeenCalledWith(
                `http://localhost:8080/friend-requests/${friendId}`,
                null,
                {
                    headers: {
                        Authorization: `Bearer ${sessionStorage.getItem('token')}`,
                        Accept: 'application/json',
                    },
                }
            );
        });
    });
});
