import React from 'react';
import { render, fireEvent, waitFor } from '@testing-library/react';
import axios from 'axios';
import {MemoryRouter, useNavigate, useParams} from 'react-router-dom';
import ForgotPassword from '../components/registration/resetPassword';

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useParams: jest.fn(),
    useNavigate: jest.fn(),
}));


jest.mock('axios');

describe('ResetPassword component', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });

    it('should render without crashing', () => {
        useParams.mockReturnValue({ token: 'someToken' });
        render(
            <MemoryRouter>
                <ForgotPassword />
            </MemoryRouter>
        );
    });

    it('should validate password and confirm password fields', async () => {
        useParams.mockReturnValue({ token: 'someToken' });
        const { getByPlaceholderText, getByText, getByLabelText } = render(
            <MemoryRouter>
                <ForgotPassword />
            </MemoryRouter>
        );

        const passwordInput = getByPlaceholderText('Enter password');
        const confirmPasswordInput = getByPlaceholderText('Confirm your password');
        const resetButton = getByText('Reset password');

        // Invalid password
        fireEvent.change(passwordInput, { target: { value: 'invalid' } });
        fireEvent.change(confirmPasswordInput, { target: { value: 'invalid' } });
        fireEvent.click(resetButton);

        expect(await waitFor(() => getByText('Password must contain at least one letter, one number, one special character, and have a minimum length of 8'))).toBeInTheDocument();

        // Valid password
        fireEvent.change(passwordInput, { target: { value: 'ValidPassword@123' } });
        fireEvent.change(confirmPasswordInput, { target: { value: 'ValidPassword@1234' } });
        fireEvent.click(resetButton);

        expect(await waitFor(() => getByText('Passwords do not match!'))).toBeInTheDocument();
    });

    it('should call the reset password API and navigate to login page on successful request', async () => {
        useParams.mockReturnValue({ token: 'someToken' });
        const mockNavigate = jest.fn();
        const mockPost = axios.post.mockResolvedValueOnce({ data: {} });

        useNavigate.mockReturnValue(mockNavigate);

        const { getByPlaceholderText, getByText } = render(
            <MemoryRouter>
                <ForgotPassword />
            </MemoryRouter>
        );

        const passwordInput = getByPlaceholderText('Enter password');
        const confirmPasswordInput = getByPlaceholderText('Confirm your password');
        const resetButton = getByText('Reset password');

        fireEvent.change(passwordInput, { target: { value: 'ValidPassword@123' } });
        fireEvent.change(confirmPasswordInput, { target: { value: 'ValidPassword@123' } });
        fireEvent.click(resetButton);

        await waitFor(() => {
            expect(mockPost).toHaveBeenCalledWith('http://localhost:8080/reset-password', {
                userToken: 'someToken',
                password: 'ValidPassword@123',
            });
            expect(mockNavigate).toHaveBeenCalledWith('/login');
        });
    });

});
