import { useEffect, useState } from "react";
import axios from "axios";
import toast from "react-hot-toast";
import { useHistory, useLocation } from "react-router-dom";

const useNoteLogic = () => {
    const location = useLocation();
    const history = useHistory();
    const [formField, setFormField] = useState({
        name: location.state?.name ? location.state.name : "",
        content: location.state?.content ? location.state.content : "",
        path: location.state?.path ? location.state.path : "",
        editing: location.state?.editing ? location.state.editing : false,
    });

    const isTextFile = () => {
        const words = formField.name.split(".");
        return words[words.length - 1] === "txt";
    };

    const checkName = () => {
        //TODO Temporary check
        return isTextFile();
    };

    const fixName = () => {
        let newName = formField.name + ".txt";
        setFormField({ ...formField, name: newName });
    };

    useEffect(() => {
        console.log(formField);
        if (isTextFile()) {
            axios
                .get("/api/get_file_content", {
                    params: { path: formField.path },
                })
                .then((response) => {
                    setFormField({ ...formField, content: response.data });
                    console.log(response);
                });
        }
    }, []);

    const submitNote = (event) => {
        event.preventDefault();
        let formData = new FormData();

        if (!checkName()) fixName();

        formData.append("name", formField.name);
        formData.append("content", formField.content);
        formData.append("path", formField.path);
        formData.append("editing", formField.editing);

        axios
            .post("/api/new_note", formData)
            .then((response) => {
                toast.success("New file was created successfully");
                history.push("");
            })
            .catch((error) => toast.error(error.response.data));
    };

    const onChange = (event) => {
        const value = event.target.value;
        setFormField({ ...formField, [event.target.name]: value });
    };

    return { submitNote, onChange, formField };
};

export default useNoteLogic;
