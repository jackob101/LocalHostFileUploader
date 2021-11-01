import React, { useRef } from "react";

const NewDirectoryForm = (props) => {
    const name = useRef();

    return (
        <form
            onSubmit={(event) => props.onCreateNewFolder(event, name)}
            className="d-flex flex-row align-items-center"
        >
            <label htmlFor="directoryName" className="p-2">
                Folder name
            </label>
            <input
                type="text"
                name="directoryName"
                id="directoryName"
                ref={name}
                required
            />
            <button type="submit" className=" m-2 btn btn-outline-primary">
                Submit
            </button>
        </form>
    );
};

export default NewDirectoryForm;
