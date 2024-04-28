import React from 'react';
import { render, fireEvent, waitFor } from '@testing-library/react';
import axios from 'axios';
import {MemoryRouter, useNavigate} from 'react-router-dom';
import ForgotPassword from '../components/registration/forgotPassword';

jest.mock('axios');
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: jest.fn(),
}));
describe('ForgotPassword component', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });

    it('should display an alert if email is not provided', () => {
        const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});

        const { getByText } = render(
            <MemoryRouter>
                <ForgotPassword />
            </MemoryRouter>
        );

        const sendEmailButton = getByText('Send email');

        fireEvent.click(sendEmailButton);

        expect(alertMock).toHaveBeenCalledWith('Please enter the email to reset the password!');

        alertMock.mockRestore();
    });

    it('should call the forgot password API and navigate to confirm-forgot-password page on successful request', async () => {
        const email = 'test@example.com';
        const mockNavigate = jest.fn();
        useNavigate.mockReturnValue(mockNavigate);
        const mockPost = axios.post.mockResolvedValueOnce({ data: {} });

        const { getByPlaceholderText, getByText } = render(
            <MemoryRouter>
                <ForgotPassword />
            </MemoryRouter>
        );

        const emailInput = getByPlaceholderText('Enter email');
        const sendEmailButton = getByText('Send email');

        fireEvent.change(emailInput, { target: { value: email } });
        fireEvent.click(sendEmailButton);

        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith('/confirm-forgot-password', { state: { email } });
        });
    });

    it('should display an alert if the forgot password API request fails', async () => {
        const email = 'test@example.com';
        const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});
        const mockPost = axios.post.mockRejectedValueOnce({ response: { data: { error: 'Error message' } } });

        const { getByPlaceholderText, getByText } = render(
            <MemoryRouter>
                <ForgotPassword />
            </MemoryRouter>
        );

        const emailInput = getByPlaceholderText('Enter email');
        const sendEmailButton = getByText('Send email');

        fireEvent.change(emailInput, { target: { value: email } });
        fireEvent.click(sendEmailButton);

        await waitFor(() => {
            expect(mockPost).toHaveBeenCalledWith(`http://localhost:8080/forgot-password/${email}`);
            expect(alertMock).toHaveBeenCalledWith('Error message');

            alertMock.mockRestore();
        });
    });
});