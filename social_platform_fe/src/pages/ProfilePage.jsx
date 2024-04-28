import React , {useState,useEffect} from "react";
import {useNavigate, useParams} from "react-router-dom";
import Header from "../components/Header";
import Sidebar from "../components/Sidebar";
import MainProfileContent from "../components/MainProfileContent";
import ChatList from "../components/chat/chatList";
import axios from 'axios';


const drawerWidth = 290;
const ProfilePage = () => {
  const navigate = useNavigate();
  let { idUser, userName } = useParams();
  const [seeChat, setSeeChat] = useState(false);

  const token = sessionStorage.getItem("token"); // Get the token from session storage or wherever it's stored

  const [profileData, setProfileData] = useState({
    description: "",
    hobbies: "",
    address: "",
    publicDetails: "",
  });

  useEffect(() => {
    const fetchProfileData = async () => {
      try {
        console.log(token);
        const response = await axios.get(
          `http://localhost:8080/detail/` + idUser,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              Accept: "application/json",
            },
          }
        );

        setProfileData(response.data.response);
      } catch (error) {
        console.error("Error fetching available dates:", error);
      }
    };
  


    fetchProfileData();

    console.log("Profile page test for " , userName)
  }, [idUser, userName, token]);


  const handleMenuClick = ()=>{
    navigate("/")
  };


  const handleHome = () => {
    navigate("/home");
  };

  const handleChat = () => {
    setSeeChat(!seeChat);
  };

  const handleViewProfile =()=>{
    navigate(`/myprofile/${idUser}`)
  };

  return (
    <div style={{ display: "flex" }}>
      <Header handleMenuClick={handleMenuClick} />
      <Sidebar
        handleViewProfile={handleViewProfile}
        handleHomeClick={handleHome}
        handleChat={handleChat}
      />
      <main style={{ flexGrow: 1, padding: "50px" }}>
        {seeChat ? (
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              marginBottom: "20px",
            }}
          >
            <ChatList />
          </div>
        ) : (
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              marginBottom: "20px",
            }}
          >
            <div style={{ display: "flex" }}>
              <MainProfileContent profileData={profileData} userId={idUser} name={userName} />
            </div>
          </div>
        )}
      </main>
    </div>
  );
};

export default ProfilePage;
