import React ,{useState, useEffect} from "react";
import { useNavigate, useParams } from "react-router-dom";
import Header from "../components/Header";
import MyProfileSidebar from "../components/MyProfileSidebar";
import MyMainProfileContent from "../components/MyMainProfileContent";
import ChatList from "../components/chat/chatList";
import axios from 'axios';
import { Toolbar } from "@mui/material";


const drawerWidth = 290;
const MyProfilePage = () => {
  const navigate = useNavigate();
  let { idUser } = useParams();
  const [seeChat, setSeeChat] = useState(false);

  const token = sessionStorage.getItem('token'); // Get the token from session storage or wherever it's stored

  const [myProfileData,setMyProfileData] = useState({description:'',hobbies:'',address:'',isPublic:''})


  useEffect(() => {
    const fetchMyProfileData = async () => {
      try {
        
        console.log(token);
        const response = await axios.get(`http://localhost:8080/detail/`+idUser,{
          headers: {
              Authorization: `Bearer ${token}`,
              Accept: 'application/json',
          }
      });
        
        setMyProfileData(response.data.response);
      } catch (error) {
        console.error('Error fetching available dates:', error);
      }
    };

    fetchMyProfileData ();
  }, [idUser, token]);


  const handleMenuClick = () => {
    navigate("/");
  };

  const handleHome = () => {
    navigate("/");
  };

  const handleChat = () =>{
    setSeeChat(!seeChat);
}


  return (
    <div style={{ display: "flex" }}>
    <Header handleMenuClick={handleMenuClick} />
    <MyProfileSidebar handleHome={handleHome}  profileData={myProfileData} handleChat={handleChat}/>
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
      <MyMainProfileContent profileData={myProfileData}/>
    </div>
    </div>
  )}
  </main>
  </div>
  )
};

export default MyProfilePage;
