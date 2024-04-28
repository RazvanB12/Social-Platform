import React from 'react';
import {render, fireEvent, waitFor} from '@testing-library/react';
import {MemoryRouter, useNavigate} from 'react-router-dom';
import axios from 'axios';
import Login from '../components/registration/login'; // Adjust the import path accordingly

jest.mock('axios');
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: jest.fn(),
}));
describe('<Login />', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });

    test('renders without crashing', () => {
        render(
            <MemoryRouter>
                <Login />
            </MemoryRouter>
        );
    });

    test('should display an alert when email and password fields are empty', () => {
        const { getByPlaceholderText, getByText } = render(
            <MemoryRouter>
                <Login />
            </MemoryRouter>
        );

        const signInButton = getByText('Sign in');

        const mockAlert = jest.spyOn(window, 'alert').mockImplementation(() => {});

        fireEvent.click(signInButton);

        expect(mockAlert).toHaveBeenCalledWith('Please enter both email and password.');

        // Restore the original alert function
        mockAlert.mockRestore();
    });


    test('successful login redirects to the correct route based on role', async () => {
        const mockNavigate = jest.fn();
        useNavigate.mockReturnValue(mockNavigate);

        // Mock the axios post request to return different roles
        axios.post.mockResolvedValueOnce({
            data: {
                response: {
                    token: 'someToken',
                    userId: 'someUserId',
                    role: 'CLIENT', // Assuming this is the client role for the test
                    name: 'Test User',
                },
            },
        });

        const { getByPlaceholderText, getByText } = render(
            <MemoryRouter>
                <Login />
            </MemoryRouter>
        );

        const emailInput = getByPlaceholderText('Enter email');
        const passwordInput = getByPlaceholderText('Enter password');
        const signInButton = getByText('Sign in');

        fireEvent.change(emailInput, { target: { value: 'test@example.com' } });
        fireEvent.change(passwordInput, { target: { value: 'password123' } });

        fireEvent.click(signInButton);

        // You might need to wait for a bit if there's asynchronous operation before navigation
        await new Promise(resolve => setTimeout(resolve, 100));

        expect(mockNavigate).toHaveBeenCalledWith('/home');
    });

    test('failed login shows error message', async () => {
        const { getByPlaceholderText, getByText } = render(
            <MemoryRouter>
                <Login />
            </MemoryRouter>
        );

        const emailInput = getByPlaceholderText('Enter email');
        const passwordInput = getByPlaceholderText('Enter password');
        const signInButton = getByText('Sign in');

        fireEvent.change(emailInput, { target: { value: 'invalid@example.com' } });
        fireEvent.change(passwordInput, { target: { value: 'invalidpassword' } });

        const mockAlert = jest.spyOn(window, 'alert').mockImplementation(() => {});

        axios.post.mockRejectedValueOnce({ response: { data: { error: 'Provided credentials are not valid' } } });

        fireEvent.click(signInButton);

        await waitFor(() => {
            expect(mockAlert).toHaveBeenCalledTimes(1);
            expect(mockAlert).toHaveBeenCalledWith('Provided credentials are not valid');
        });

        mockAlert.mockRestore();
    });


});
