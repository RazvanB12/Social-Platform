import "./App.css";
import AdminPage from "./components/admin/AdminPage";
import Register from "./components/registration/register";
import Login from "./components/registration/login";
import ResetPassword from "./components/registration/resetPassword";
import ConfirmForgotPassword from "./components/registration/confirmForgotPassword";
import ForgotPassword from "./components/registration/forgotPassword";
import ReportPage from "./components/admin/ReportPage";
import ProfilePage from "./pages/ProfilePage"
import MyProfilePage from "./pages/MyProfilePage"
import Feed from "./pages/Feed"
import PhotoAlbumPage from "./pages/PhotoAlbumPage"
import MyPhotoAlbumPage from "./pages/MyPhotoAlbumPage"
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import UserContent from "./components/admin/UserContent";

function App() {

  return (
      <div>
          <Router>
              <Routes>
                  <Route path='/' element={<Login/>} />
                  <Route path='/register' element={<Register />} />
                  <Route path='/login' element={<Login />} />
                  <Route path='/admin' element={<AdminPage />} />
                  <Route path='/forgot-password' element={<ForgotPassword/>} />
                  <Route path='/confirm-forgot-password' element={<ConfirmForgotPassword />} />
                  <Route path="/resetPassword/:token" element={<ResetPassword />} />
                  <Route path="/home" element={<Feed />} />
                  <Route path="/reportpage" element={<ReportPage />} />
                  <Route path="/profile/:idUser/:userName" element={<ProfilePage />} />
                  <Route path="/myprofile/:idUser" element={<MyProfilePage />} />
                  <Route path="/album/:userId/:albumName" element={<PhotoAlbumPage/>}/>
                  <Route path="/myalbum/:userId/:albumName" element={<MyPhotoAlbumPage/>}/>
                  <Route path="/allcontent/:idUser" element={<UserContent />} />
              </Routes>
          </Router>
      </div>
  );
}

export default App;
