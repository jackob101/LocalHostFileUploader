import React, { useRef } from "react";

const NewDirectoryForm = (props) => {
    const name = useRef();

    return (
        <form
            onSubmit={(event) => props.onCreateNewFolder(event, name)}
            className="container"
        >
            <div className="row">
                <div className="col-12 col-lg-7 m-lg-0 mb-3 p-0 me-lg-4">
                    <label htmlFor="directoryName" className="form-label">
                        Create new folder
                    </label>
                    <input
                        className="form-control"
                        type="text"
                        name="directoryName"
                        id="directoryName"
                        placeholder="Folder name"
                        ref={name}
                        required
                    />
                </div>
                <div className="col-12 col-lg-4 d-flex p-0 align-items-end">
                    <button
                        type="submit"
                        className="btn btn-outline-primary flex-grow-1"
                    >
                        Submit
                    </button>
                </div>
            </div>
        </form>
    );
};

export default NewDirectoryForm;
