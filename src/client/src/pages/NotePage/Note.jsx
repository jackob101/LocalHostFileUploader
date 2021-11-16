import React from "react";
import useNoteLogic from "./NoteLogic";

const Note = () => {
    const { submitNote, onChange, formField, onCancel } = useNoteLogic();

    return (
        <div className="d-flex flex-column col-lg-8 mx-auto">
            <h2 className="text-center my-3">Note editor</h2>
            <form onSubmit={submitNote} className="d-flex flex-column w-80">
                <label htmlFor="name" className="form-label">
                    File name
                </label>
                <input
                    disabled={formField.editing}
                    type="text"
                    id="name"
                    name="name"
                    className="form-control"
                    onChange={onChange}
                    value={formField.name}
                />

                <label htmlFor="content" className="form-label mt-3">
                    File content
                </label>
                <textarea
                    name="content"
                    id="content"
                    cols="30"
                    rows="10"
                    className="form-control"
                    onChange={onChange}
                    value={formField.content}
                ></textarea>
                <button type="submit" className="btn btn-primary my-3">
                    Submit
                </button>
                <button
                    type="button"
                    className="btn btn-outline-danger"
                    onClick={onCancel}
                >
                    Cancel
                </button>
            </form>
        </div>
    );
};

export default Note;
