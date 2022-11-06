package com.elcom.plus.user.service.impl;

import com.elcom.crbt.cmp.ws.commons.WSResult;
import com.elcom.plus.common.util.CollectionsUtil;
import com.elcom.plus.user.controller.VideoController;
import com.elcom.plus.user.dto.response.ListMyVideo;
import com.elcom.plus.user.dto.response.MyVideoData;
import com.elcom.plus.user.dto.response.VideoDetailResponse;
import lombok.SneakyThrows;
import org.apache.commons.codec.language.AbstractCaverphone;
import org.apache.commons.io.FileUtils;
import com.elcom.plus.common.util.file.FileUtil;
import com.elcom.plus.common.util.request.ItemExcelsRequest;
import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.user.client.MeCallClient;
import com.elcom.plus.user.config.db.ConnectionManager;
import com.elcom.plus.user.dao.CommonDao;
import com.elcom.plus.user.dao.UserDao;
import com.elcom.plus.user.dao.VideoDao;
import com.elcom.plus.user.dto.request.*;
import com.elcom.plus.user.dto.response.MyVideoResponse;
import com.elcom.plus.user.entity.User;
import com.elcom.plus.user.service.VideoService;
import com.elcom.plus.user.transcode.TranscoderClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {

    private static ConnectionManager connectionManager = new ConnectionManager();
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
    private MeCallClient meCallClient;
    private UserDao userDao;
    private VideoDao videoDao;
    private CommonDao commonDao;

    public VideoServiceImpl(MeCallClient meCallClient, UserDao userDao, VideoDao videoDao, CommonDao commonDao) {
        this.meCallClient = meCallClient;
        this.userDao = userDao;
        this.videoDao = videoDao;
        this.commonDao = commonDao;
    }

    @Value("${folder.name}")
    private String folder;

    @Value("${domain.file}")
    protected String domainFile;

    @Override
    public Response myVideo(String id, int type, int videoId, int skip, int take) {
        Response response = new Response();
        User user = userDao.findById(id);
        List<MyVideoResponse> listVideoData = new ArrayList<>();
        ListMyVideo listMyVideo = new ListMyVideo();
        VideoDetailResponse videoDetail = null;
        if (Objects.isNull(user) || user.getId() == 0) {
            response.setCode(8002);
            response.setMessage(commonDao.getMessage("USER", 8002));
            return response;
        }
        String phone = user.getPhone();
        //get list video upload
        if (type == 0) {
            logger.info("[{}]========== {} ==========", phone, "Loading video upload.");
            listMyVideo = videoDao.getVideoUploadByUserId(Integer.valueOf(id), phone, videoId, skip, take);
        }
        //get list video like
        if (type == 1) {
            logger.info("[{}]========== {} ==========", phone, "Loading video like.");
            listMyVideo = videoDao.getVideoLikeByUserID(Integer.valueOf(id), videoId, skip, take, phone);
        }
        //get list video buy
        if (type == 2) {
            logger.info("[{}]========== {} ==========", phone, "Loading video buy.");
            listMyVideo = userDao.findVideoBuy(Integer.valueOf(id), videoId, phone, skip, take);
        }
        //get list video gift
        if (type == 3) {
            logger.info("[{}]========== {} ==========", phone, "Loading video gift.");
            listMyVideo = userDao.findVideoGift(Integer.valueOf(id), videoId, phone, skip, take);
        }
        //get video detail
//        if (videoId > 0 && type == 3) {
//            videoDetail = userDao.findVideoGiftById(videoId, Integer.valueOf(id), phone);
//        } else if (videoId > 0) {
//            videoDetail = userDao.findVideoById(videoId, Integer.valueOf(id));
//        }
//        MyVideoData videoObj = new MyVideoData();
//        videoObj.setListVideoData(listVideoData);
//        videoObj.setVideoDetail(videoDetail);
        response.setCode(0);
        response.setMessage(commonDao.getMessage("USER", 9002));
        response.setData(listMyVideo);
        return response;
    }

//    @Async
//    public Response uploadVideoTest(MultipartFile fileVideo, MultipartFile fileImage, String videoInfo, String id) {
//        Response response = new Response();
//        final long start = System.currentTimeMillis();
//        try {
//            User user = userDao.findById(id);
//            if (Objects.isNull(user)) {
//                logger.info("[]========== User is null ==========");
//                response.setCode(1);
//                response.setMessage(commonDao.getMessage("USER", 8002));
//            }
//            logger.info("[xxx]========== input fileVideo ==========: {}", fileVideo);
//            logger.info("[xxx]========== input fileImage ==========: {}", fileImage);
//            String phone = user.getPhone();
//            logger.info("[{}]========== input fileVideo ==========: {}", phone, fileVideo);
//            UploadVideo videoInfoObj = new ObjectMapper().readValue(videoInfo, UploadVideo.class);
//            logger.info("[{}]========== input videoInfoObj ==========: {}", phone, videoInfoObj);
//            File fileVideoCv = new File(Objects.requireNonNull(fileVideo.getOriginalFilename()));
//            logger.info("[{}]========== create file video done ==========", phone);
//            File fileImageCv = new File(Objects.requireNonNull(fileImage.getOriginalFilename()));
//            logger.info("[{}]========== create file image done ==========", phone);
////
//            String fileNameVideo = FileUtil.getFileName(fileVideoCv);
//            String fileNameImage = FileUtil.getFileName(fileImageCv);
//
//            // create temporary folder
//            SimpleDateFormat dateTime = new SimpleDateFormat("yyyyMMdd_hhmmss.SSSZ");
//            String dateInString = dateTime.format(new Date());
//            String strDir = id + dateInString;
//            // get path file video in temp dir
//            String pathDirTemp = Files.createTempDirectory(strDir).toAbsolutePath().toString();
////            String pathDirTemp = "C:\\Users\\Admin\\Desktop\\ct\\test";
//            logger.info("[{}]========== pathDirTemp ==========: {}", phone, pathDirTemp);
//            String filePathTempVideo = fileVideo.toString();
//            logger.info("[{}]========== filePathTempVideo ==========: {}", phone, filePathTempVideo);
////                String filePathTempVideo = pathDirTemp + "/" + fileNameVideo;
//
//            String musicPath = "";
//            String pathVideoMusic = "";
//            String filePathVideoText = "";
////                String filePathVideo = userDao.getPathCp(2) + fileNameVideo;
////                String filePathImage = userDao.getPathCp(2) + fileNameImage;
//            String pathMusicOut = pathDirTemp + "\\music_" + strDir + ".mp3";
//            String filePathVideo = folder + File.separator + fileNameVideo;
//            String filePathImage = folder + File.separator + fileNameImage;
//
//            if (Objects.isNull(videoInfoObj.getMusicSelected()) && Objects.isNull(videoInfoObj.getTextEditor())) {
//                // saves the file on disk
////                fileVideo.transferTo(new File(filePathVideo));
//                logger.info("[{}]========== save file video done ==========: {}", phone, filePathVideo);
//            }
////            else {
//                // save file video to temp dir
////                fileVideo.transferTo(new File(filePathTempVideo));
////                logger.info("[{}]========== save file video to temp dir done ==========: {}", phone, filePathTempVideo);
////            }
//
//
//            if (!Objects.isNull(videoInfoObj.getMusicSelected())) {
//                String urlMusic = videoInfoObj.getMusicSelected().getSongPath();
//                logger.info("[{}]========== urlMusic ==========: {}", phone, urlMusic);
////                    musicPath = urlMusic != null? urlMusic.replace(domainFile, "/u02") : "";
//                musicPath = urlMusic;
//                logger.info("[{}]========== musicPath ==========: {}", phone, musicPath);
//                // trim music
//                if (!Objects.isNull(videoInfoObj.getMusicCrop())) {
//                    MusicCropDto musicCropDto = videoInfoObj.getMusicCrop();
//                    CollectionsUtil.trimMusic(musicPath, "00:00:" + musicCropDto.getFrom(), "00:00:" + musicCropDto.getTo(), pathMusicOut);
//                } else {
//                    CollectionsUtil.trimMusic(musicPath, "", "", pathMusicOut);
//                }
//                //add music to video
//                if (!Objects.isNull(videoInfoObj.getTextEditor())) {
//                    pathVideoMusic = pathDirTemp + "\\video_music_" + fileNameVideo;
//                    CollectionsUtil.addMusic(pathMusicOut, filePathTempVideo, pathVideoMusic);
//                    logger.info("[{}]========== save file video with music done ==========: {}", phone, pathVideoMusic);
//                } else {
//                    CollectionsUtil.addMusic(pathMusicOut, filePathTempVideo, filePathVideo);
//                    logger.info("[{}]========== save file video with music done ==========: {}", phone, filePathVideo);
//                }
//
//            } else if (!Objects.isNull(videoInfoObj.getTextEditor())) {
//                //add text to video
//                String pathVideoInput = "";
//                if (pathVideoMusic.isEmpty()) {
//                    pathVideoInput = filePathTempVideo;
//                } else {
//                    pathVideoInput = pathVideoMusic;
//                }
//                String textInput = videoInfoObj.getTextEditor().getText();
//                String textColor = videoInfoObj.getTextEditor().getTextColor();
//                String bgColor = videoInfoObj.getTextEditor().getBgColor();
//                String font = videoInfoObj.getTextEditor().getFont();
//                float posX = videoInfoObj.getTextEditor().getPosX();
//                float posY = videoInfoObj.getTextEditor().getPosY();
//                int videoW = videoInfoObj.getTextEditor().getVideoW();
//                int videoH = videoInfoObj.getTextEditor().getVideoH();
//                String pathVideoResize = pathDirTemp + "\\resize_" + fileNameVideo;
//                CollectionsUtil.addTextToVideo(pathVideoInput, pathVideoResize, textInput, font, textColor, bgColor, videoW, videoH, posX, posY, filePathVideo);
//                logger.info("[{}]========== save file video with text done ==========: {}", phone, filePathVideo);
//            }
//
//            //delete file in folder temp
//            logger.info("[{}]========== start delete folder temp done ==========", phone);
////            File folder = new File(pathDirTemp);
//            logger.info("[{}]========== Folder temp delete ==========: {}", phone, pathDirTemp);
////                FileUtils.deleteDirectory(folder);
////                folder.delete();
//            logger.info("[{}]========== delete folder temp done ==========", phone);
//            //save file image to disk
////            fileImage.transferTo(new File(filePathImage));
////            logger.info("[{}]========== save file image done ==========: {}", phone, filePathImage);
//
////                String pathExcel = userDao.getPathConfig("PathFileExcel") + "END_USER" + File.separator + FileUtil.getFileNameXls(fileVideoCv);
//            String pathExcel = folder + File.separator + FileUtil.getFileNameXls(fileVideoCv);
//            Calendar date = Calendar.getInstance();
//            date.add(Calendar.YEAR, 10);
//            Date dateLastAdd = date.getTime();
//            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            String strDate = dateFormat.format(dateLastAdd);
//            Boolean createFileExcels = CollectionsUtil.writeVideoInfoToExcelFile(new ItemExcelsRequest("END_USER", videoInfoObj.getTitle(), fileNameVideo, "END_USER", strDate, "0", fileNameImage, videoInfoObj.getHashtags(), videoInfoObj.getDuration(), user.getUsername(),
//                videoInfoObj.getMusicSelected().getId()), pathExcel);
//            if (createFileExcels) {
//                logger.info("[{}]========== save file excel done ==========: {}", phone, pathExcel);
//            } else {
//                logger.info("[{}]========== save file excel fail ==========: {}", phone, pathExcel);
//            }
//
//            WSResult resultValidate = null;
////                try {
////                    TranscoderClient transcoderClient = TranscoderClient.getInstance();
////                    resultValidate = transcoderClient.validateDataFile("", pathExcel, 2, user.getId().intValue());
////                    logger.info("[{}]========== Single Upload =>btnCreate_action()=>resultValidate ==========: {}", phone, resultValidate.getResultCode());
////                    if (resultValidate.getResultCode() == 0) {
////                        response.setCode(0);
////                        response.setMessage(commonDao.getMessage("USER", 9000));
////                    }
////                } catch (Exception ex) {
////                    logger.info("[{}]========== Error ==========: {}", phone, ex);
////                    ex.printStackTrace();
////                }
//            logger.info("[{}]========== Upload video done ==========", phone);
//            logger.info("==================End [{}]==================", phone);
//
//        } catch (Exception e) {
//            logger.error("[]================== Error ==================: ", e);
//            e.printStackTrace();
//        };
//        logger.info("========== Elapsed time ==========: {}", (System.currentTimeMillis() - start));
//        logger.info("Done!!!");
//        return response;
//    }

    @Override
    public Response uploadVideo(MultipartFile fileVideo, MultipartFile fileImage, String videoInfo, String id) {
        Response response = new Response();
        try {
            User user = userDao.findById(id);
            if (Objects.isNull(user)) {
                logger.info("[]========== User is null ==========");
                response.setCode(1);
                response.setMessage(commonDao.getMessage("USER", 8002));
                return response;
            }
            String phone = user.getPhone();
            UploadVideo videoInfoObj = new ObjectMapper().readValue(videoInfo, UploadVideo.class);
            logger.info("[{}]========== input videoInfoObj ==========: {}", phone, videoInfoObj);
            File fileVideoCv = new File(Objects.requireNonNull(fileVideo.getOriginalFilename()));
            logger.info("[{}]========== create file video done ==========", phone);
            File fileImageCv = new File(Objects.requireNonNull(fileImage.getOriginalFilename()));
            logger.info("[{}]========== create file image done ==========", phone);

            String fileNameVideo = FileUtil.getFileName(fileVideoCv);
            String fileNameImage = FileUtil.getFileName(fileImageCv);

            // create temporary folder
            SimpleDateFormat dateTime = new SimpleDateFormat("yyyyMMdd_hhmmss.SSSZ");
            String dateInString = dateTime.format(new Date());
            String strDir = id + dateInString;
            // get path file video in temp dir
            String pathDirTemp = Files.createTempDirectory(strDir).toAbsolutePath().toString();
//            String filePathTempVideo = pathDirTemp + "\\" + fileNameVideo;
            String filePathTempVideo = pathDirTemp + "/" + fileNameVideo;

            String filePathVideo = userDao.getPathCp(2) + fileNameVideo;
            String musicPath = "";
            String pathVideoMusic = "";
            String filePathVideoText = "";
            String filePathImage = userDao.getPathCp(2) + fileNameImage;
            String pathMusicOut = pathDirTemp + "/music_" + strDir + ".mp3";
//            String filePathVideo = folder + File.separator + fileNameVideo;
//            String filePathImage = folder + File.separator + fileNameImage;

            if (Objects.isNull(videoInfoObj.getMusicSelected()) && Objects.isNull(videoInfoObj.getTextEditor())) {
                // saves the file on disk
                fileVideo.transferTo(new File(filePathVideo));
                logger.info("[{}]========== save file video done ==========: {}", phone, filePathVideo);
            } else {
                // save file video to temp dir
                fileVideo.transferTo(new File(filePathTempVideo));
                logger.info("[{}]========== save file video to temp dir done ==========: {}", phone, filePathTempVideo);
            }

            if (!Objects.isNull(videoInfoObj.getMusicSelected())) {
                String urlMusic = videoInfoObj.getMusicSelected().getSongPath();
                musicPath = urlMusic != null ? urlMusic.replace(domainFile, "/u02") : "";
                logger.info("[{}]========== musicPath ==========: {}", phone, musicPath);
                // trim music
                if (!Objects.isNull(videoInfoObj.getMusicCrop())) {
                    MusicCropDto musicCropDto = videoInfoObj.getMusicCrop();
                    CollectionsUtil.trimMusic(musicPath, "00:00:" + Math.round(musicCropDto.getFrom()), "00:00:" + Math.round(musicCropDto.getTo()), pathMusicOut);
                } else {
                    CollectionsUtil.trimMusic(musicPath, "", "", pathMusicOut);
                }
                //add music to video
                if (!Objects.isNull(videoInfoObj.getTextEditor())) {
                    pathVideoMusic = pathDirTemp + "/video_music_" + fileNameVideo;
                    CollectionsUtil.addMusic(pathMusicOut, filePathTempVideo, pathVideoMusic);
                    logger.info("[{}]========== save file video with music done ==========: {}", phone, pathVideoMusic);
                } else {
                    CollectionsUtil.addMusic(pathMusicOut, filePathTempVideo, filePathVideo);
                    logger.info("[{}]========== save file video with music done ==========: {}", phone, filePathVideo);
                }

            }
            if (!Objects.isNull(videoInfoObj.getTextEditor())) {
                //add text to video
                String pathVideoInput = "";
                if (pathVideoMusic.isEmpty()) {
                    pathVideoInput = filePathTempVideo;
                } else {
                    pathVideoInput = pathVideoMusic;
                }
                String textInput = videoInfoObj.getTextEditor().getText();
                String textColor = videoInfoObj.getTextEditor().getTextColor();
                String bgColor = videoInfoObj.getTextEditor().getBgColor();
                String font = videoInfoObj.getTextEditor().getFont();
                float posX = videoInfoObj.getTextEditor().getPosX();
                float posY = videoInfoObj.getTextEditor().getPosY();
                int videoW = videoInfoObj.getTextEditor().getVideoW();
                int videoH = videoInfoObj.getTextEditor().getVideoH();
                String pathVideoResize = pathDirTemp + "/resize_" + fileNameVideo;
                CollectionsUtil.addTextToVideo(pathVideoInput, pathVideoResize, textInput, font, textColor, bgColor, videoW, videoH, posX, posY, filePathVideo);
                logger.info("[{}]========== save file video with text done ==========: {}", phone, filePathVideo);
            }

            //delete file in folder temp
            logger.info("[{}]========== start delete folder temp done ==========", phone);
            File folder = new File(pathDirTemp);
            logger.info("[{}]========== Folder temp delete ==========: {}", phone, folder);
            FileUtils.deleteDirectory(folder);
            folder.delete();
            logger.info("[{}]========== delete folder temp done ==========", phone);
            //save file image to disk
            fileImage.transferTo(new File(filePathImage));
            logger.info("[{}]========== save file image done ==========: {}", phone, filePathImage);

            String pathExcel = userDao.getPathConfig("PathFileExcel") + "END_USER" + File.separator + FileUtil.getFileNameXls(fileVideoCv);
//            String pathExcel = folder + File.separator + FileUtil.getFileNameXls(fileVideoCv);
            Calendar date = Calendar.getInstance();
            date.add(Calendar.YEAR, 10);
            Date dateLastAdd = date.getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = dateFormat.format(dateLastAdd);
            logger.info("[{}]================ music id ================: {}", phone, videoInfoObj.getMusicSelected().getId());
            Boolean createFileExcels = CollectionsUtil.writeVideoInfoToExcelFile(new ItemExcelsRequest("END_USER", videoInfoObj.getTitle(), fileNameVideo, "END_USER", strDate, "0", fileNameImage, videoInfoObj.getHashtags(), videoInfoObj.getDuration(), user.getUsername(),
                            videoInfoObj.getMusicSelected().getId()),
                    pathExcel);
            if (createFileExcels) {
                logger.info("[{}]========== save file excel done ==========: {}", phone, pathExcel);
            } else {
                logger.info("[{}]========== save file excel fail ==========: {}", phone, pathExcel);
            }

            WSResult resultValidate = null;
            try {
                TranscoderClient transcoderClient = TranscoderClient.getInstance();
                resultValidate = transcoderClient.validateDataFile("", pathExcel, 2, user.getId().intValue());
                logger.info("[{}]========== Single Upload =>btnCreate_action()=>resultValidate ==========: {}", phone, resultValidate.getResultCode());
                if (resultValidate.getResultCode() == 0) {
                    userDao.updateMusic(videoInfoObj.getMusicSelected().getId(), phone);
                    response.setCode(0);
                    response.setMessage(commonDao.getMessage("USER", 9000));
                }
            } catch (Exception ex) {
                logger.info("[{}]========== Error ==========: {}", phone, ex);
                ex.printStackTrace();
            }
            logger.info("[{}]========== Upload video done ==========", phone);
            logger.info("==================End [{}]==================", phone);

        } catch (Exception e) {
            logger.info("========== Error ==========: ", e);
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response deleteVideo(String id, String phone, VideoRequest videoRequest) {
        Response response = new Response();
        response.setCode(999);
        logger.info("[{}]========== videoRequest ==========: {}", phone, videoRequest);
        try {
            if (videoRequest.getVideoId() == null) {
                logger.info("[{}]========== Field videoId is required ==========", phone);
                return response;
            } else if (videoRequest.getMsisdn().isEmpty()) {
                logger.info("[{}]========== Field msisdn is required ==========", phone);
                return response;
            } else if (videoRequest.getMsisdnReceiver().isEmpty()) {
                logger.info("[{}]========== Field msisdnReceiver is required ==========", phone);
                return response;
            } else if (videoRequest.getId() == null) {
                logger.info("[{}]========== Field id is required ==========", phone);
                return response;
            }
            boolean deleteVideoUpload = videoDao.deleteVideoUpload(Integer.valueOf(id), phone, String.valueOf(videoRequest.getVideoId()));
            if (deleteVideoUpload) {
                response.setCode(0);
                response.setMessage(commonDao.getMessage("VIDEO", 9001));
                logger.info("[{}]========== message ==========: {}", phone, commonDao.getMessage("VIDEO", 9001));
                logger.info("==================End [{}]==================", phone);
            }
        } catch (Exception e) {
            logger.info("[{}]========== Error ==========: {}", phone, e);
            e.printStackTrace();
        }
        return response;
    }

}
