/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */
// main.js

// Ensure the DOM is fully loaded before running scripts
$(document).ready(function() {
    console.log("main.js loaded successfully");

    // Example: Handle certificate status filter change
    $('#statusFilter').on('change', function() {
        const selectedStatus = $(this).val();
        window.location.href = `${window.location.pathname}?status=${selectedStatus}`;
    });

    // Example: Confirm certificate revocation
    $('.revoke-btn').on('click', function(e) {
        e.preventDefault();
        const certificateId = $(this).data('certificate-id');
        if (confirm(`Are you sure you want to revoke certificate ID ${certificateId}?`)) {
            $.post(window.location.pathname, {
                action: 'revoke',
                certificateId: certificateId
            }, function(response) {
                location.reload(); // Reload the page to reflect changes
            }).fail(function() {
                alert('Failed to revoke certificate. Please try again.');
            });
        }
    });

    // Example: Confirm certificate renewal
    $('.renew-btn').on('click', function(e) {
        e.preventDefault();
        const certificateId = $(this).data('certificate-id');
        if (confirm(`Are you sure you want to renew certificate ID ${certificateId}?`)) {
            $.post(window.location.pathname, {
                action: 'renew',
                certificateId: certificateId
            }, function(response) {
                location.reload(); // Reload the page to reflect changes
            }).fail(function() {
                alert('Failed to renew certificate. Please try again.');
            });
        }
    });

    // Example: Show success/error messages (if they exist)
    const successMessage = $('#successMessage').text();
    const errorMessage = $('#errorMessage').text();
    if (successMessage) {
        alert(successMessage);
    }
    if (errorMessage) {
        alert(errorMessage);
    }
});

