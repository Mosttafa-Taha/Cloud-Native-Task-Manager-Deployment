import axios from 'axios';
import { useFormik } from 'formik';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import UserProfileImg from "../../images/Admin-Profile-Vector-PNG-Clipart.png"; // fallback image

export default function EmployeeComment() {
    const { id } = useParams();

    const [task, setTask] = useState({});
    const [allComments, setAllComments] = useState([]);

    const userComment = { comment: "" };

    // Fetch task details
    async function getData() {
        try {
            const { data } = await axios.get(`/api/employee/oneTask/${id}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('tokenJwt')}`
                },
                withCredentials: true
            });
            const formattedDate = new Date(data.dueDate).toISOString().split('T')[0];
            data.dueDate = formattedDate;
            setTask(data);
            await getAllComments(); // load comments after task
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        getData();
    }, []);

    // Fetch all comments
    async function getAllComments() {
        try {
            const { data } = await axios.get(`/api/employee/comments/${id}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('tokenJwt')}`
                },
                withCredentials: true
            });
            setAllComments(data);
        } catch (error) {
            console.error(error);
        }
    }

    // Create comment
    async function createComment(values, { resetForm }) {
        try {
            await axios.post(`/api/employee/createComment/${id}`,
                { content: values.comment },
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('tokenJwt')}`
                    },
                    withCredentials: true
                }
            );
            resetForm();
            await getAllComments();
        } catch (error) {
            console.error('Error:', error);
        }
    }

    const formik = useFormik({
        initialValues: userComment,
        onSubmit: createComment,
        validate: values => {
            const errors = {};
            if (!values.comment) errors.comment = "Comment is required";
            return errors;
        }
    });

    return (
        <div className="container mt-5">

            {/* Task Details */}
            <div className="row">
                <div className="col-md-9">
                    <div className="my_task shadow bg-white py-5 px-4 rounded-3">
                        <h2 className='text-primary'>{task?.title}</h2>
                        <p className="text-muted">{task?.description}</p>

                        <div className="task_content border-2 border-top border-gray p-2 d-flex justify-content-between align-items-center">
                            <div>
                                <p>Deadline: <span className='fw-bold'>{new Date(task?.dueDate).toLocaleDateString('en-US', { day: 'numeric', month: 'short', year: 'numeric' })}</span></p>
                                <p>Priority: <span className='fw-bold'>{task?.priority} High</span></p>
                            </div>
                            <div>
                                <p>Employee: <span className='fw-bold'>{task?.employeeName}</span></p>
                                <p>Status: <span className='fw-bold'>{task?.taskStatus}</span></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Create Comment Form */}
            <div className="row mt-5">
                <div className="col-md-9">
                    <div className="comment shadow bg-white py-5 px-4 rounded-3">
                        <h2>Create Comment:</h2>
                        <div className="shadow-sm p-4">
                            <form onSubmit={formik.handleSubmit}>
                                <label htmlFor="comment">Comment:</label>
                                <textarea
                                    id="comment"
                                    name="comment"
                                    className='form-control mb-3'
                                    value={formik.values.comment}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                />
                                {formik.errors.comment && formik.touched.comment && (
                                    <div className="alert alert-danger">{formik.errors.comment}</div>
                                )}
                                <div className="d-flex justify-content-center align-items-center">
                                    <button type="submit" className='btn btn-outline-primary'>Publish Comment</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            {/* Comments List */}
            <div className="row mt-4 gy-3">
                {allComments.map(comment => (
                    <div className="col-md-9" key={comment.id}>
                        <div className="my_task shadow bg-white py-5 px-4 rounded-3">
                            <div className="admin_img w-25 d-flex align-items-center">
                                <img
                                    className="rounded-circle"
                                    src={comment.user?.avatar || UserProfileImg}
                                    alt={comment.user?.name || "User"}
                                    style={{ width: "50px", height: "50px" }}
                                />
                                <h2 className="ms-3">{comment.postedBy}</h2>
                            </div>
                            <p className="text-muted ms-5 ps-2">
                                {new Date(comment.createdAt).toLocaleDateString('en-US', { day: 'numeric', month: 'short', year: 'numeric' })}
                            </p>
                            <p className="ms-5 ps-2">{comment.content}</p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
