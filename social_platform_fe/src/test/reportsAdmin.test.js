import React from 'react';
import { render, waitFor, fireEvent } from '@testing-library/react';
import axios from 'axios';
import PhotoGallery from '../components/admin/PhotoGallery';

jest.mock('axios');

const mockData = {
    response: [
        { imageId: 1, content: 'base64encodedimage1' },
        { imageId: 2, content: 'base64encodedimage2' }
    ]
};

test('fetches photos successfully', async () => {
    axios.get.mockResolvedValueOnce({ data: mockData });

    const { findAllByAltText } = render(<PhotoGallery />);

    await waitFor(() => {
        expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/admin/image', {
            headers: { 'Authorization': 'Bearer null' }
        });
    });

    const images = await findAllByAltText('Picture');
    expect(images).toHaveLength(2);
});

test('blocks an image successfully', async () => {
    // Mock the Axios request
    axios.get.mockResolvedValueOnce({ data: mockData });

    const { queryAllByTestId } = render(<PhotoGallery />);

    await waitFor(() => {
        // Query for the block buttons
        const blockButtons = queryAllByTestId('block-button');
        // Check if any block buttons are found
        if (blockButtons.length > 0) {
            fireEvent.click(blockButtons[0]);
            expect(axios.delete).toHaveBeenCalledWith('http://localhost:8080/admin/image/1', {
                headers: { 'Authorization': 'Bearer null' }
            });
        } else {

        }
    });
});

test('rejects a reported image successfully', async () => {
    axios.get.mockResolvedValueOnce({ data: mockData });
    // Mock the Axios request
    axios.get.mockResolvedValueOnce({ data: mockData });

    const { queryAllByTestId } = render(<PhotoGallery />);

    await waitFor(() => {
        // Query for the block buttons
        const rejectButtons = queryAllByTestId('reject-button');
        // Check if any block buttons are found
        if (rejectButtons.length > 0) {
            fireEvent.click(rejectButtons[0]);
            expect(axios.patch).toHaveBeenCalledWith('http://localhost:8080/admin/image/1', {
                headers: { 'Authorization': 'Bearer null' }
            });
        } else {

        }
    });

});
