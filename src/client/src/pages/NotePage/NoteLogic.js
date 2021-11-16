import { useEffect, useState } from "react";
import axios from "axios";
import toast from "react-hot-toast";
import { useHistory, useLocation } from "react-router-dom";

const useNoteLogic = () => {
    const location = useLocation();
    const history = useHistory();

    console.log(location);
    const [formField, setFormField] = useState({
        name: location.state?.name ? location.state.name : "",
        content: location.state?.content ? location.state.content : "",
        path: location.state?.path ? location.state.path : "",
        editing: location.state?.editing ? location.state.editing : false,
    });

    const isTextFile = (name) => {
        const words = name.split(".");
        return words[words.length - 1] === "txt";
    };

    const checkName = (name) => {
        //TODO Temporary check
        return isTextFile(name);
    };

    const fixName = (name) => {
        let newName = name + ".txt";
        return newName;
    };

    useEffect(() => {
        if (isTextFile(formField.name)) {
            axios
                .get("/api/get_file_content", {
                    params: { path: formField.path },
                })
                .then((response) => {
                    setFormField({ ...formField, content: response.data });
                });
        }
    }, []);

    const submitNote = (event) => {
        event.preventDefault();
        let formData = new FormData();
        let name = formField.name;
        let path = formField.path;

        if (!checkName(name)) name = fixName(name);
        if (!formField.editing) {
            path += path.length > 0 ? "/" : "";
            path += name;
        }

        formData.append("name", name);
        formData.append("content", formField.content);
        formData.append("path", path);
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

    const onCancel = () => {
        history.push("");
    };

    return { submitNote, onChange, formField, onCancel };
};

export default useNoteLogic;
