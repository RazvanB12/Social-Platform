import React from 'react';
import { render, fireEvent, waitFor } from '@testing-library/react';
import UsersTable from '../components/admin/UsersTable';

test('fetches users successfully', async () => {
    global.fetch = jest.fn().mockResolvedValueOnce({
        json: async () => ({
            response: [{ id: 1, email: 'test@example.com', firstName: 'John', lastName: 'Doe', active: true }],
        }),
    });

    const { getByText } = render(<UsersTable />);

    await waitFor(() => {
        expect(getByText('test@example.com')).toBeInTheDocument();
        expect(getByText('John')).toBeInTheDocument();
        expect(getByText('Doe')).toBeInTheDocument();
        expect(getByText('Yes')).toBeInTheDocument();
    });
});

test('handles page change', async () => {
    const { getByTestId } = render(<UsersTable />);

    // Wait for any asynchronous operations to complete
    await waitFor(() => {
        const skipNextButton = getByTestId('SkipNextIcon');
        expect(skipNextButton).toBeInTheDocument();
        expect(skipNextButton).toBeEnabled(); // Check if button is clickable
        fireEvent.click(skipNextButton);
    });

});
test('renders users table and performs cancel correctly', async () => {
    // Mock the fetch API
    global.fetch = jest.fn().mockResolvedValueOnce({
        json: () => Promise.resolve({
            response: [
                { id: 1, email: 'test1@example.com', firstName: 'John', lastName: 'Doe', active: true },
                { id: 2, email: 'test2@example.com', firstName: 'Jane', lastName: 'Doe', active: false }
            ]
        })
    });

    // Mock the alert function
    global.alert = jest.fn();

    // Render the component
    const { getByText, getByTestId } = render(<UsersTable />);

    // Wait for the data to be fetched and the component to update
    await waitFor(() => {
        // Ensure that the users table is rendered
        expect(getByText('Email')).toBeInTheDocument();
        expect(getByText('First Name')).toBeInTheDocument();
        expect(getByText('Last Name')).toBeInTheDocument();
        expect(getByText('Active')).toBeInTheDocument();
        expect(getByText('Actions')).toBeInTheDocument();

        // Ensure that two users are displayed
        expect(getByText('test1@example.com')).toBeInTheDocument();
        expect(getByText('test2@example.com')).toBeInTheDocument();
    });

    // Simulate cancel action
    fireEvent.click(getByTestId('cancel-button'));

    // Ensure that the fetch request is made with the correct parameters
    await waitFor(() => {
        expect(global.fetch).toHaveBeenCalledWith('http://localhost:8080/admin/delete-client-account/1', {
            method: 'DELETE',
            headers: { Authorization: 'Bearer null' },
        });
        expect(global.alert).toHaveBeenCalledWith("Delete done!");
    });

});
test('renders users table and performs activate correctly', async () => {
    global.fetch = jest.fn().mockResolvedValueOnce({
        json: () => Promise.resolve({
            response: [
                { id: 1, email: 'test1@example.com', firstName: 'John', lastName: 'Doe', active: false },
            ]
        })
    });
    global.alert = jest.fn();
    const { getByText, getByTestId } = render(<UsersTable />);

    await waitFor(() => {
        expect(getByText('Email')).toBeInTheDocument();
        expect(getByText('First Name')).toBeInTheDocument();
        expect(getByText('Last Name')).toBeInTheDocument();
        expect(getByText('Active')).toBeInTheDocument();
        expect(getByText('Actions')).toBeInTheDocument();
        expect(getByText('test1@example.com')).toBeInTheDocument();
    });

    fireEvent.click(getByTestId('activate-button'));

    await waitFor(() => {
        expect(global.fetch).toHaveBeenCalledWith('http://localhost:8080/activate-client-account/1', {
            method: 'PATCH',
            headers: { Authorization: 'Bearer null' },
        });
        expect(global.alert).toHaveBeenCalledWith("Validation done!");
    });
});

test('renders users table and performs reject correctly', async () => {
    global.fetch = jest.fn().mockResolvedValueOnce({
        json: () => Promise.resolve({
            response: [
                { id: 1, email: 'test1@example.com', firstName: 'John', lastName: 'Doe', active: false },
            ]
        })
    });

    global.alert = jest.fn();

    const { getByText,getByTestId } = render(<UsersTable />);

    await waitFor(() => {
        expect(getByText('Email')).toBeInTheDocument();
        expect(getByText('First Name')).toBeInTheDocument();
        expect(getByText('Last Name')).toBeInTheDocument();
        expect(getByText('Active')).toBeInTheDocument();
        expect(getByText('Actions')).toBeInTheDocument();
        expect(getByText('test1@example.com')).toBeInTheDocument();
    });

    fireEvent.click(getByTestId('reject-button'));

    await waitFor(() => {
        expect(global.fetch).toHaveBeenCalledWith('http://localhost:8080/reject-client-account/1', {
            method: 'PATCH',
            headers: { Authorization: 'Bearer null' },
        });
        expect(global.alert).toHaveBeenCalledWith("Validation rejected!");
    });
});

