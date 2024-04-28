import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import axios from 'axios';
import FeedZone from '../components/FeedZone';
import {MemoryRouter, useNavigate} from 'react-router-dom';

jest.mock('axios');
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: jest.fn(),
}));
const mockSessionStorage = {
    getItem: jest.fn(),
    setItem: jest.fn(),
    clear: jest.fn(),
};

Object.defineProperty(window, 'sessionStorage', {
    value: mockSessionStorage,
});
describe('<FeedZone />', () => {
    it('renders welcome message when accessing for the first time', async () => {
        mockSessionStorage.getItem.mockReturnValueOnce('false'); // Simulate first-time access
        const mockNavigate = jest.fn();
        useNavigate.mockReturnValue(mockNavigate); // Mock useNavigate

        render(
            <MemoryRouter>
                <FeedZone />
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(screen.getByText('Welcome to your feed!')).toBeInTheDocument();
        });
    });
    it('navigate to login if token is not available', async () => {
        jest.clearAllMocks();

        const mockNavigate = jest.fn();
        useNavigate.mockReturnValue(mockNavigate);
        mockSessionStorage.getItem.mockReturnValueOnce('false'); // Simulate first-time access

        render(
            <MemoryRouter>
                <FeedZone />
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith('/login');
        });
    });

    it('fetches and displays the feed list', async () => {
        const mockNavigate = jest.fn();
        mockNavigate.mockReturnValue(mockNavigate);

        require('react-router-dom').useNavigate.mockReturnValue(mockNavigate);

        mockSessionStorage.getItem.mockImplementation((key) => {
            switch (key) {
                case 'userId':
                    return 'someUserId';
                case 'token':
                    return 'someToken';
                default:
                    return null;
            }
        });

        const mockFeedList = [
            {
                profilePicture: { content: 'someBase64' },
                user: { firstName: 'John', lastName: 'Doe' },
                image: { uploadDate: new Date().toISOString(), type: 'image/jpeg', content: 'someBase64' },
            },
        ];

        axios.get.mockResolvedValueOnce({ data: { response: mockFeedList } });

        render(
            <MemoryRouter>
                <FeedZone />
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/newsFeed/someUserId', {
                headers: {
                    Authorization: 'Bearer someToken',
                    Accept: 'application/json',
                },
            });
            expect(screen.getByText('John Doe')).toBeInTheDocument();
        });
    });



    it('displays a profile picture if available', async () => {
        const mockNavigate = jest.fn();
        mockNavigate.mockReturnValue(mockNavigate);

        useNavigate.mockReturnValue(mockNavigate);

        const mockFeedList = [
            {
                profilePicture: { content: 'someBase64' },
                user: { firstName: 'John', lastName: 'Doe' },
                image: { uploadDate: new Date().toISOString(), type: 'image/jpeg', content: 'someBase64' },
            },
        ];

        axios.get.mockResolvedValueOnce({ data: { response: mockFeedList } });

        render(
            <MemoryRouter>
                <FeedZone />
            </MemoryRouter>
        );

        await waitFor(() => {
            const profilePicture = screen.getByAltText('Profile Picture');
            expect(profilePicture).toBeInTheDocument();
            expect(profilePicture.src).toContain('data:\'image/jpeg;base64,someBase64'); // Note the correct URL format
        });
    });

});
