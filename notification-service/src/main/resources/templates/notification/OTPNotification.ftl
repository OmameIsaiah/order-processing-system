<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Verify Email Address</title>
</head>

<body style="background-color: whitesmoke; font-family: Arial, sans-serif; color: #5E6670;  padding: 50px 0;">
<!-- <div style="text-align: center; margin: 30px auto;">
    <img style="width: 80px;" src="https://res.cloudinary.com/dseez6xvg/image/upload/v1693901685/forthcm/logo_irrtls.png" alt="Fort">
  </div> -->

<div style="background-color: white; width: 80%; margin: auto;  border-radius: 6px; border:thin solid #f1f1f1;">
    <div style="padding: 20px 0; background-color: #580abe; color:#f5f5f5; text-align: center; font-size: 15px; border-radius: 6px 6px 0 0;">
        <!-- <span style="margin-right: 10px;">
    <svg width="30px" height="30px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" stroke="#e4cdcd">
    <g id="SVGRepo_bgCarrier" stroke-width="0"/>
    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round" stroke="#CCCCCC" stroke-width="0.144"/>
    <g id="SVGRepo_iconCarrier"> <path d="M2 16C2 13.1716 2 11.7574 2.87868 10.8787C3.75736 10 5.17157 10 8 10H16C18.8284 10 20.2426 10 21.1213 10.8787C22 11.7574 22 13.1716 22 16C22 18.8284 22 20.2426 21.1213 21.1213C20.2426 22 18.8284 22 16 22H8C5.17157 22 3.75736 22 2.87868 21.1213C2 20.2426 2 18.8284 2 16Z" stroke="#ffffff" stroke-width="1.2"/> <circle cx="12" cy="16" r="2" stroke="#ffffff" stroke-width="1.2"/> <path d="M6 10V8C6 4.68629 8.68629 2 12 2C14.7958 2 17.1449 3.91216 17.811 6.5" stroke="#ffffff" stroke-width="1.2" stroke-linecap="round"/> </g>
    </svg>
</span> -->
        <span>Verify your email address</span>

    </div>
    <div style="padding: 40px;">
        <div>
            <h1 style="color: black; font-size: 18px; font-weight: 600">Dear ${name},</h1>
            <p style="font-size: 15px; margin-bottom: 10px; margin-top: 10px; line-height: 30px; color: #666666;">
                Thank you for registering with us! To complete your email verification, please use the OTP below:</p>
        </div>
        <div style="padding: 20px; color: #580abe; text-align: center; font-weight: 600; font-size: 20px;">One-time Password
        </div>
        <div style="text-align: center; background-color: #f9f9f9; border-radius: 8px; color:black; margin-top: 10px; margin-bottom: 20px; justify-content: center; font-size: 45px; font-weight: 600; padding-top: 40px; padding-bottom: 40px; ">
            <#list otp?split( "") as digit>
            <span style="margin-right: 5px;">${digit}</span>
        </#list>
    </div>
</div>
<div style="padding: 40px;">
    <div style="font-size: 15px; margin-bottom: 10px; margin-top: -40px; text-align: left; line-height: 30px; color: #666666;">
        Kindly note that this OTP is temporary and will expire in few minutes. If you did not create an account with us.
        Ltd., please disregard this email.
    </div>
    <div style="background-color: #ffffff; color: #666666; margin-bottom: 30px; margin-top: 40px;">
        <div style="margin-bottom: 9px;">Best regards,</div>
        <div>Order Processing System</div>
    </div>
</div>
</div>

<!-- <div style="text-align: center; color: #666666; padding: 40px 0;">
<a href="https://forthcm.com" style="text-decoration: none; color: #01de60; font-weight: bold;">Unsubscribe</a> from these alerts
</div> -->
</body>

        </html>