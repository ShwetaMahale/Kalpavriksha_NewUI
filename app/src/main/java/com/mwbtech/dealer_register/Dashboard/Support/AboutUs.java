package com.mwbtech.dealer_register.Dashboard.Support;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.R;


public class AboutUs extends AppCompatActivity {

    TextView txt;
    ListView listView1, listView2, listView3, listView4;

    String[] advertisetxt = {"Advertise with us"};
    String[] termstxt = {"Terms of use"};
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);



        String versionName = "3.2";
        String[] versiontxt = {"Version " + versionName};
        final String copyrights = getString(R.string.copy_right);
        String[] copytrighttxt = {copyrights};
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_list_txt, versiontxt);
        ArrayAdapter adapter1 = new ArrayAdapter<String>(this,
                R.layout.activity_list_txt, advertisetxt);
        ArrayAdapter adapter2 = new ArrayAdapter<String>(this,
                R.layout.activity_list_txt, copytrighttxt);

        ArrayAdapter adapter3 = new ArrayAdapter<String>(this,
                R.layout.activity_list_txt, termstxt);
        txt = findViewById(R.id.txt_content);
        back = findViewById(R.id.back);
        listView1 = findViewById(R.id.list1);
        listView2 = findViewById(R.id.list2);
        listView3 = findViewById(R.id.list3);
        listView4 = findViewById(R.id.list4);
        listView1.setAdapter(adapter);
        listView2.setAdapter(adapter1);
        listView3.setAdapter(adapter2);
        listView4.setAdapter(adapter3);

        listView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SetTErmUseDataToUI();

            }

        });
        back.setOnClickListener((v)->onBackPressed());

        txt.setText("We @ MWB Technologies India Pvt Ltd, empower traders, merchants & small businesses into higher levels of efficiency & profitability. With simple, intuitive business automation solutions, with the power of internet and mobile, our wide range of offerings are built for Desktop, Web, And Mobile.\n\n" +
                "Our Mission is to provide Retail, Distribution, B2B, B2C and Manufacturing Businesses on the Technology path helping them stay competitive in business.\n\n" +
                "Kalpavriksha is an Exclusive Business Networking Group. \n\n" +
                "The Application has been developed and designed to encourage Business activities globally. The name Kalpavriksha itself " +
                "states it’s to exhilarate business between all the people from Multifarious Businesses.\n\n" +
                "We have a clear objective to first register every Individual of the Application across the country who is engaged " +
                "in numerous business activities and into a different league of the supply chain like manufacturing, dealership, distribution," +
                "etc… and then connect for every business inquiry in any part of the country by simply searching for the item and type of supplier.\n\n" +
                "While developing this App, we also gave more importance to a new and fast-growing category of Professionals in our Network under which we can get the Country’s Best Doctors, Chartered Accountants, Lawyers, Architects, Engineers, and Software Developers registered on Kalpavriksha and prove their skills in every Professional Field throughout the Country.\n\n" +
                "Kalpavriksha App is built on an Extensive study of Different Business Models, Products, and Items trying to cover more than 1.5 Lakhs Products and continuously thriving to increase this product furthermore. We have tried our best to cover every possible product and service and willing to add more through your support and suggestions.\n\n" +
                "The vision and exposition of this app are to see that the business enlarges among your connections. Uplifting coast to coast across the board at the national level.  \n" +
                "Depending upon the geographical area of the operation, in feature various city wise chapters can be enhanced, and regularly registered users can have meetings and socialize. Which will give a personal emotional touch to this entire process of getting our people connected.\n\n" +
                "We hope your registration on Kalpavriksha is smooth and easy and you find answers to every business or professional Enquiry.\n\n" +
                "We will be happy to hear more from you in making our Kalpavriksha more Steady and Ready for every demand made." +
                "Thank You.");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class).putExtra("isNewUser", false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        AboutUs.this.finish();
    }

    private void SetTErmUseDataToUI() {
        String txt1 = "These Terms of Use constitute a legally binding agreement made between you, whether personally or on behalf of an entity (“you”) and MWB Technologies India Pvt. Ltd, doing business as MWB Tech (\"MWB Tech\", “we”, “us”, or “our”), concerning your access to and use of Application ( Kalpavriksha -Jain Business Network here after referred as App/Application) as well as any other media form, media channel, mobile application related, linked, or otherwise connected there to (collectively, the “App”).You agree that by accessing the App, you have read, understood, and agreed to be bound by all of these Terms of Use. IF YOU DO NOT AGREE WITH ALL OF THESE TERMS OF USE, THEN YOU ARE EXPRESSLY PROHIBITED FROM USING THE APP AND YOU MUST DISCONTINUE USE IMMEDIATELY.\n\n" +
                "Supplemental terms and conditions or documents that may be posted on the App from time to time are hereby expressly incorporated herein by reference. We reserve the right, in our sole discretion, to make changes or modifications to these Terms of Use at any time and for any reason. We will alert you about any changes by updating the “Last updated” date of these Terms of Use, and you waive any right to receive specific notice of each such change. It is your responsibility to periodically review these Terms of Use to stay informed of updates. You will be subject to, and will be deemed to have been made aware of and to have accepted, the changes in any revised Terms of Use by your continued use of the App after the date such revised Terms of Use are posted.\n\n" +
                "The information provided on the App is not intended for distribution to or use by any person or entity in any jurisdiction or country where such distribution or use would be contrary to law or regulation or which would subject us to any registration requirement within such jurisdiction or country. Accordingly,those persons who choose to access the App from other locations do so on their own initiative and are solely responsible for compliance with local laws, if and to the extent local laws are applicable.\n\n" +
                "The App is intended for users who are at least 18 years old. Persons under the age of 18 are not permitted to use or register for the App.\n";

        String txt2 = "Unless otherwise indicated, the App is our proprietary property and all source code, databases,functionality, software, App designs, audio, video, text, photographs, and graphics on the App(collectively, the “Content”) and the trademarks, service marks, and logos contained therein (the “Marks”) are owned or controlled by us or licensed to us, and are protected by copyright and trademark laws and various other intellectual property rights and unfair competition laws of India, international copyright laws, and international conventions. The Content and the Marks are provided on the App “AS IS” for your information and personal use only. Except as expressly provided in these Terms of Use, no part of the App and no Content or Marks may be copied, reproduced, aggregated, republished,uploaded, posted, publicly displayed, encoded, translated, transmitted, distributed, sold, licensed, or otherwise exploited for any commercial purpose whatsoever, without our express prior written permission.\n\n" +
                "Provided that you are eligible to use the App, you are granted a limited license to access and use the App and to download or print a copy of any portion of the Content to which you have properly gained access solely for your personal, non-commercial use. We reserve all rights not expressly granted to you in and to the App, the Content and the Marks.";
        String txt3 = "By using the App, you represent and warrant that: (1) all registration information you submit will be true, accurate, current, and complete; (2) you will maintain the accuracy of such information and promptly update such registration information as necessary; (3) you have the legal capacity and you agree to comply with these Terms of Use; (4) you are not a minor in the jurisdiction in which you reside;(5) you will not access the App through automated or non-human means, whether through a bot, script or otherwise; (6) you will not use the App for any illegal or unauthorized purpose; and (7) your use of the App will not violate any applicable law or regulation.\n\n" +
                "If you provide any information that is untrue, inaccurate, not current, or incomplete, we have the right to suspend or terminate your account and refuse any and all current or future use of the App (or any portion thereof).";

        String txt4 = "You may be required to register with the App. You agree to keep your password confidential and will be responsible for all use of your account and password. We reserve the right to remove, reclaim, or change a username you select if we determine, in our sole discretion, that such username is inappropriate, obscene, or otherwise objectionable";

        String txt5 = "You may not access or use the App for any purpose other than that for which we make the App available. The App may not be used in connection with any commercial endeavors except those that are specifically endorsed or approved by us.\n\n" +
                "As a user of the App, you agree not to:\n\n" +
                "1. Systematically retrieve data or other content from the App to create or compile, directly or indirectly, a collection, compilation, database, or directory without written permission from us.\n" +
                "2. Trick, defraud, or mislead us and other users, especially in any attempt to learn sensitive account\n" +
                "information such as user passwords.\n" +
                "3. Circumvent, disable, or otherwise interfere with security-related features of the App, including\n" +
                "features that prevent or restrict the use or copying of any Content or enforce limitations on the use\n" +
                "of the App and/or the Content contained therein.\n" +
                "4. Disparage, tarnish, or otherwise harm, in our opinion, us and/or the App.\n" +
                "5. Use any information obtained from the App in order to harass, abuse, or harm another person.\n" +
                "6. Make improper use of our support services or submit false reports of abuse or misconduct.\n" +
                "7. Use the App in a manner inconsistent with any applicable laws or regulations.\n" +
                "8. Engage in unauthorized framing of or linking to the App.\n" +
                "9. Upload or transmit (or attempt to upload or to transmit) viruses, Trojan horses, or other material, including excessive use of capital letters and spamming (continuous posting of repetitive text), that interferes with any party’s uninterrupted use and enjoyment of the App or modifies, impairs, disrupts, alters, or interferes with the use, features, functions, operation, or maintenance of the App.\n" +
                "10. Engage in any automated use of the system, such as using scripts to send comments or\n" +
                "messages, or using any data mining, robots, or similar data gathering and extraction tools.\n" +
                "11. Delete the copyright or other proprietary rights notice from any Content.\n" +
                "12. Attempt to impersonate another user or person or use the username of another user.\n" +
                "13. Sell or otherwise transfer your profile.\n" +
                "14. Upload or transmit (or attempt to upload or to transmit) any material that acts as a passive or active information collection or transmission mechanism, including without limitation, clear graphics interchange formats (“gifs”), 1×1 pixels, web bugs, cookies, or other similar devices (sometimes referred to as “spyware” or “passive collection mechanisms” or “pcms”).\n" +
                "15. Interfere with, disrupt, or create an undue burden on the App or the networks or services connected to the App.\n" +
                "16. Harass, annoy, intimidate, or threaten any of our employees or agents engaged in providing any portion of the App to you.\n" +
                "17. Attempt to bypass any measures of the App designed to prevent or restrict access to the App, or any portion of the App.\n" +
                "18. Copy or adapt the App’s software, including but not limited to Flash, PHP, HTML, JavaScript, or other code.\n" +
                "19. Decipher, decompile, disassemble, or reverse engineer any of the software comprising or in any way making up a part of the App.\n" +
                "20. Use a buying agent or purchasing agent to make purchases on the App.\n" +
                "21. Make any unauthorized use of the App, including collecting usernames and/or email addresses of users by electronic or other means for the purpose of sending unsolicited email, or creating user accounts by automated means or under false pretenses.\n" +
                "22. Use the App as part of any effort to compete with us or otherwise use the App and/or the Content for any revenue-generating endeavor or commercial enterprise.";

        String txt6 = "The App may invite you to chat, contribute to, or participate in blogs, message boards, online forums, and other functionality, and may provide you with the opportunity to create, submit, post, display, transmit, perform, publish, distribute, or broadcast content and materials to us or on the App, including but not limited to text, writings, video, audio, photographs, graphics, comments, suggestions, or personal information or other material (collectively, \"Contributions\"). Contributions may be viewable by other users of the App and through third-party Apps. As such, any Contributions you transmit may be treated as non-confidential and non-proprietary. When you create or make available any Contributions, you thereby represent and warrant that:\n\n" +
                "1. The creation, distribution, transmission, public display, or performance, and the accessing, downloading, or copying of your Contributions do not and will not infringe the proprietary rights, including but not limited to the copyright, patent, trademark, trade secret, or moral rights of any third party.\n" +
                "2. You are the creator and owner of or have the necessary licenses, rights, consents, releases, and permissions to use and to authorize us, the App, and other users of the App to use your Contributions in any manner contemplated by the App and these Terms of Use.\n" +
                "3. You have the written consent, release, and/or permission of each and every identifiable individual person in your Contributions to use the name or likeness of each and every such identifiable individual person to enable inclusion and use of your Contributions in any manner contemplated by the App and these Terms of Use.\n" +
                "4. Your Contributions are not false, inaccurate, or misleading.\n" +
                "5. Your Contributions are not unsolicited or unauthorized advertising, promotional materials, pyramid schemes, chain letters, spam, mass mailings, or other forms of solicitation.\n" +
                "6. Your Contributions are not obscene, lewd, lascivious, filthy, violent, harassing, libelous, slanderous, or otherwise objectionable (as determined by us).\n" +
                "7. Your Contributions do not ridicule, mock, disparage, intimidate, or abuse anyone.\n" +
                "8. Your Contributions do not advocate the violent overthrow of any government or incite, encourage, or threaten physical harm against another.\n" +
                "9. Your Contributions do not violate any applicable law, regulation, or rule.\n" +
                "10. Your Contributions do not violate the privacy or publicity rights of any third party.\n" +
                "11. Your Contributions do not contain any material that solicits personal information from anyone under the age of 18 or exploits people under the age of 18 in a sexual or violent manner.\n" +
                "12. Your Contributions do not violate any applicable law concerning child pornography, or otherwise intended to protect the health or well-being of minors;\n" +
                "13. Your Contributions do not include any offensive comments that are connected to race, national origin, gender, sexual preference, or physical handicap.\n" +
                "14. Your Contributions do not otherwise violate, or link to material that violates, any provision of these Terms of Use, or any applicable law or regulation.\n\n" +
                "Any use of the App in violation of the foregoing violates these Terms of Use and may result in, among other things, termination or suspension of your rights to use the App";

        String txt7 = "By posting your Contributions to any part of the App, you automatically grant, and you represent and warrant that you have the right to grant, to us an unrestricted, unlimited, irrevocable, perpetual, nonexclusive, transferable, royalty-free, fully-paid, worldwide right, and license to host, use, copy, reproduce, disclose, sell, resell, publish, broadcast, retitle, archive, store, cache, publicly perform, publicly display, reformat, translate, transmit, excerpt (in whole or in part), and distribute such Contributions (including, without limitation, your image and voice) for any purpose, commercial, advertising, or otherwise, and to prepare derivative works of, or incorporate into other works, such Contributions, and grant and authorize sublicenses of the foregoing. The use and distribution may occur in any media formats and through any media channels.\n\n" +
                "This license will apply to any form, media, or technology now known or hereafter developed, and includes our use of your name, company name, and franchise name, as applicable, and any of the trademarks, service marks, trade names, logos, and personal and commercial images you provide. You waive all moral rights in your Contributions, and you warrant that moral rights have not otherwise been asserted in your Contributions.\n\n" +
                "We do not assert any ownership over your Contributions. You retain full ownership of all of your Contributions and any intellectual property rights or other proprietary rights associated with your Contributions. We are not liable for any statements or representations in your Contributions provided by you in any area on the App. You are solely responsible for your Contributions to the App and you expressly agree to exonerate us from any and all responsibility and to refrain from any legal action against us regarding your Contributions.\n\n" +
                "We have the right, in our sole and absolute discretion, (1) to edit, redact, or otherwise change any Contributions; (2) to re-categorize any Contributions to place them in more appropriate locations on the App; and (3) to pre-screen or delete any Contributions at any time and for any reason, without notice.We have no obligation to monitor your Contributions.";

        String txt8 = "We may provide you areas on the App to leave reviews or ratings. When posting a review, you must comply with the following criteria: (1) you should have firsthand experience with the person/entity being reviewed; (2) your reviews should not contain offensive profanity, or abusive, racist, offensive, or hate language; (3) your reviews should not contain discriminatory references based on religion, race, gender, national origin, age, marital status, sexual orientation, or disability; (4) your reviews should not contain references to illegal activity; (5) you should not be affiliated with competitors if posting negative reviews; (6) you should not make any conclusions as to the legality of conduct; (7) you may not post any false or misleading statements; and (8) you may not organize a campaign encouraging others to post reviews, whether positive or negative.\n\n" +
                "We may accept, reject, or remove reviews in our sole discretion. We have absolutely no obligation to screen reviews or to delete reviews, even if anyone considers reviews objectionable or inaccurate. Reviews are not endorsed by us, and do not necessarily represent our opinions or the views of any of our affiliates or partners. We do not assume liability for any review or for any claims, liabilities, or losses resulting from any review. By posting a review, you hereby grant to us a perpetual, non-exclusive, worldwide, royalty-free, fully-paid, assignable, and sub licensable right and license to reproduce, modify, translate, transmit by any means, display, perform, and/or distribute all content relating to reviews.";

        String txt9 = "You acknowledge and agree that any questions, comments, suggestions, ideas, feedback, or other information regarding the App (\"Submissions\") provided by you to us are non-confidential and shall become our sole property. We shall own exclusive rights, including all intellectual property rights, and shall be entitled to the unrestricted use and dissemination of these Submissions for any lawful purpose, commercial or otherwise, without acknowledgment or compensation to you. You hereby waive all moral rights to any such Submissions, and you hereby warrant that any such Submissions are original with you or that you have the right to submit such Submissions. You agree there shall be no recourse against us for any alleged or actual infringement or misappropriation of any proprietary right in your Submissions.";

        String txt10 = "We reserve the right, but not the obligation, to: (1) monitor the App for violations of these Terms of Use; (2) take appropriate legal action against anyone who, in our sole discretion, violates the law or these Terms of Use, including without limitation, reporting such user to law enforcement authorities; (3)in our sole discretion and without limitation, refuse, restrict access to, limit the availability of, or disable(to the extent technologically feasible) any of your Contributions or any portion thereof; (4) in our sole discretion and without limitation, notice, or liability, to remove from the App or otherwise disable all files and content that are excessive in size or are in any way burdensome to our systems; and (5)otherwise manage the App in a manner designed to protect our rights and property and to facilitate the proper functioning of the App.";

        String txt11 = "We care about data privacy and security. By using the App, you agree to be bound by our Privacy Policy posted on the App, which is incorporated into these Terms of Use. Please be advised the App is hosted in India. If you access the App from any other region of the world with laws or other requirements governing personal data collection, use, or disclosure that differ from applicable laws in India, then through your continued use of the App, you are transferring your data to India, and you agree to have your data transferred to and processed in India.";

        String txt12 = "We respect the intellectual property rights of others. If you believe that any material available on or through the App infringes upon any copyright you own or control, please immediately notify us using the contact information provided below (a “Notification”). A copy of your Notification will be sent to the person who posted or stored the material addressed in the Notification. Please be advised that pursuant to applicable law you may be held liable for damages if you make material misrepresentations in a Notification. Thus, if you are not sure that material located on or linked to by the App infringes your copyright, you should consider first contacting an attorney.";

        String txt13 = "These Terms of Use shall remain in full force and effect while you use the App. WITHOUT LIMITING ANY OTHER PROVISION OF THESE TERMS OF USE, WE RESERVE THE RIGHT TO, IN OUR SOLE DISCRETION AND WITHOUT NOTICE OR LIABILITY, DENY ACCESS TO AND USE OF THE APP (INCLUDING BLOCKING CERTAIN IP ADDRESSES), TO ANY PERSON FOR ANY REASON OR FOR NO REASON, INCLUDING WITHOUT LIMITATION FOR BREACH OF ANY REPRESENTATION, WARRANTY,OR COVENANT CONTAINED IN THESE TERMS OF USE OR OF ANY APPLICABLE LAW OR REGULATION.WE MAY TERMINATE YOUR USE OR PARTICIPATION IN THE APP OR DELETE YOUR ACCOUNT AND ANY CONTENT OR INFORMATION THAT YOU POSTED AT ANY TIME, WITHOUT WARNING, IN OUR SOLE DISCRETION.\n\n" +
                "If we terminate or suspend your account for any reason, you are prohibited from registering and creating a new account under your name, a fake or borrowed name, or the name of any third party,even if you may be acting on behalf of the third party. In addition to terminating or suspending your account, we reserve the right to take appropriate legal action, including without limitation pursuing civil, criminal, and injunctive redress.\n";
        String txt14 = "These Terms shall be governed by and construed and enforced in accordance with the laws of India.Subject to other provisions in this Clause, courts in [Hubli] shall have exclusive jurisdiction over all issues arising out of these Terms or the use of the Services.\n" +
                "Any controversies, conflicts, disputes, or differences arising out of these Terms shall be resolved by arbitration in [Hubli] in accordance with the Arbitration and Conciliation Act, 1996 for the time being in force, which is deemed to be incorporated by reference in this Clause. The tribunal shall consist of one arbitrator appointed by the Company. The language of the arbitration shall be English.The parties to the arbitration shall keep the arbitration confidential and not disclose to any person, other than on a need to basis or to legal advisors, unless required to do so by law. The decision of the arbitrator shall be final and binding on all the Parties hereto.";

        String txt15 = "We reserve the right to change, modify, or remove the contents of the App at any time or for any reason at our sole discretion without notice. However, we have no obligation to update any information on our App. We also reserve the right to modify or discontinue all or part of the App without notice at any time.We will not be liable to you or any third party for any modification, price change, suspension, or discontinuance of the App.\n\n" +
                "We cannot guarantee the App will be available at all times. We may experience hardware, software, or other problems or need to perform maintenance related to the App, resulting in interruptions, delays, or errors. We reserve the right to change, revise, update, suspend, discontinue, or otherwise modify the App at any time or for any reason without notice to you. You agree that we have no liability whatsoever for any loss, damage, or inconvenience caused by your inability to access or use the App during any downtime or discontinuance of the App. Nothing in these Terms of Use will be construed to obligate us to maintain and support the App or to supply any corrections, updates, or releases in connection therewith.";
        String txt16 = "These Terms shall be governed by and defined following the laws of India. MWB Technologies India Pvt Ltd and yourself irrevocably consent that the courts of India shall have exclusive jurisdiction to resolve any dispute which may arise in connection with these terms.\n";

        String txt17 = "You agree to irrevocably submit all disputes related to Terms or the relationship established by this Agreement to the jurisdiction of the India courts. MWB Technologies India Pvt. Ltd shall also maintain the right to bring proceedings as to the substance of the matter in the courts of the country where you reside or, if these Terms are entered into in the course of your trade or profession, the state of your principal place of business.";

        String txt18 = "There may be information on the App that contains typographical errors, inaccuracies, or omissions,including descriptions, pricing, availability, and various other information. We reserve the right to correct any errors, inaccuracies, or omissions and to change or update the information on the App at any time, without prior notice.";

        String txt19 = "THE APP IS PROVIDED ON AN AS-IS AND AS-AVAILABLE BASIS. YOU AGREE THAT YOUR USE OF THE APP AND OUR SERVICES WILL BE AT YOUR SOLE RISK. TO THE FULLEST EXTENT PERMITTED BY LAW, WE DISCLAIM ALL WARRANTIES, EXPRESS OR IMPLIED, IN CONNECTION WITH THE APP AND YOUR USE THEREOF, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTABILITY,FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT. WE MAKE NO WARRANTIES OR REPRESENTATIONS ABOUT THE ACCURACY OR COMPLETENESS OF THE APP’S CONTENT OR THE LINKED TO THE APP AND WE WILL ASSUME NO LIABILITY OR RESPONSIBILITY FOR ANY (1) ERRORS, MISTAKES, OR INACCURACIES OF CONTENT AND MATERIALS, (2) PERSONAL INJURY OR PROPERTY DAMAGE, OF ANY NATURE WHATSOEVER, RESULTING FROM YOUR ACCESS TO AND USE OF THE APP, (3) ANY UNAUTHORIZED ACCESS TO OR USE OF OUR SECURE SERVERS AND/OR ANY AND ALL PERSONAL INFORMATION AND/OR FINANCIAL INFORMATION STORED THEREIN, (4) ANY INTERRUPTION OR CESSATION OF TRANSMISSION TO OR FROM THE APP, (5) ANY BUGS, VIRUSES, TROJAN HORSES, OR THE LIKE WHICH MAY BE TRANSMITTED TO OR THROUGH THE APP BY ANY THIRD PARTY, AND/OR (6) ANY ERRORS OR OMISSIONS IN ANY CONTENT AND MATERIALS OR FOR ANY LOSS OR DAMAGE OF ANY KIND INCURRED AS A RESULT OF THE USE OF ANY CONTENT POSTED, TRANSMITTED, OR OTHERWISE MADE AVAILABLE VIA THE APP. WE DO NOT WARRANT, ENDORSE, GUARANTEE, OR ASSUME RESPONSIBILITY FOR ANY PRODUCT OR SERVICE ADVERTISED OR OFFERED BY A THIRD PARTY THROUGH THE APP, ANY HYPERLINKED APP, OR MOBILE APPLICATION FEATURED IN ANY BANNER OR OTHER ADVERTISING, AND WE WILL NOT BE A PARTY TO OR IN ANY WAY BE RESPONSIBLE FOR MONITORING ANY TRANSACTION BETWEEN YOU AND ANY THIRD-PARTY PROVIDERS OF PRODUCTS OR SERVICES. AS WITH THE PURCHASE OF A PRODUCT OR SERVICE THROUGH ANY MEDIUM OR IN ANY ENVIRONMENT, YOU SHOULD USE YOUR BEST JUDGMENT AND EXERCISE CAUTION WHERE APPROPRIATE.";

        String txt20 = "IN NO EVENT WILL WE OR OUR DIRECTORS, EMPLOYEES, OR AGENTS BE LIABLE TO YOU OR ANY THIRD PARTY FOR ANY DIRECT, INDIRECT, CONSEQUENTIAL, EXEMPLARY, INCIDENTAL, SPECIAL, OR PUNITIVE DAMAGES, INCLUDING LOST PROFIT, LOST REVENUE, LOSS OF DATA, OR OTHER DAMAGES ARISING FROM YOUR USE OF THE APP, EVEN IF WE HAVE BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.";

        String txt21 = "You agree to defend, indemnify, and hold us harmless, including our subsidiaries, affiliates, and all of our respective officers, agents, partners, and employees, from and against any loss, damage, liability, claim,or demand, including reasonable attorneys’ fees and expenses, made by any third party due to or arising out of: (1) your Contributions; (2) use of the App; (3) breach of these Terms of Use; (4) any breach of your representations and warranties set forth in these Terms of Use; (5) your violation of the rights of a third party, including but not limited to intellectual property rights; or (6) any overt harmful act toward any other user of the App with whom you connected via the App. Notwithstanding the foregoing, we reserve the right, at your expense, to assume the exclusive defense and control of any matter for which you are required to indemnify us, and you agree to cooperate, at your expense, with our defense of such claims. We will use reasonable efforts to notify you of any such claim, action, or proceeding which is subject to this indemnification upon becoming aware of it.";

        String txt22 = "We will maintain certain data that you transmit to the App for the purpose of managing the performance of the App, as well as data relating to your use of the App. Although we perform regular routine backups of data, you are solely responsible for all data that you transmit or that relates to any activity you have undertaken using the App. You agree that we shall have no liability to you for any loss or corruption of any such data, and you hereby waive any right of action against us arising from any such loss or corruption of such data.\n";

        String txt23 = "Visiting the App, sending us emails, and completing online forms constitute electronic communications.You consent to receive electronic communications, and you agree that all agreements, notices, disclosures, and other communications we provide to you electronically, via email and on the App, satisfy any legal requirement that such communication be in writing. YOU HEREBY AGREE TO THE USE OF ELECTRONIC SIGNATURES, CONTRACTS, ORDERS, AND OTHER RECORDS, AND TO ELECTRONIC DELIVERY OF NOTICES, POLICIES, AND RECORDS OF TRANSACTIONS INITIATED OR COMPLETED BY US OR VIA THE APP. You hereby waive any rights or requirements under any statutes, regulations, rules, ordinances, or other laws in any jurisdiction which require an original signature or delivery or retention of non-electronic records, or to payments or the granting of credits by any means other than electronic means.";

        String txt24 = "These Terms of Use and any policies or operating rules posted by us on the App or in respect to the App constitute the entire agreement and understanding between you and us (MWB Technologies India Pvt.Ltd) . Our failure to exercise or enforce any right or provision of these Terms of Use shall not operate as a waiver of such right or provision. These Terms of Use operate to the fullest extent permissible by law. We may assign any or all of our rights and obligations to others at any time. We shall not be responsible or liable for any loss, damage, delay, or failure to act caused by any cause beyond our reasonable control. If any provision or part of a provision of these Terms of Use is determined to be unlawful, void, or unenforceable, that provision or part of the provision is deemed severable from these Terms of Use and does not affect the validity and enforceability of any remaining provisions. There is no joint venture, partnership, employment or agency relationship created between you and us as a result of these Terms of Use or use of the App. You agree that these Terms of Use will not be construed against us by virtue of having drafted them. You hereby waive any and all defenses you may have based on the electronic form of these Terms of Use and the lack of signing by the parties hereto to execute these Terms of Use.";

        String txt25 = "In order to resolve a complaint regarding the App or to receive further information regarding use of the App, please contact us at:";

        String txt26 = "MWB Technologies India Pvt Ltd\n" +
                "WB Plaza 1st Floor NCM Deshpande Nagar\n" +
                "Hubli, Karnataka 580029\n" +
                "India\n" +
                "support@mwbtechindia.com\n\n";
        Dialog mDialog = new Dialog(AboutUs.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.term_of_use);
        TextView text1 = mDialog.findViewById(R.id.txt1);
        TextView text2 = mDialog.findViewById(R.id.txt2);
        TextView text3 = mDialog.findViewById(R.id.txt3);
        TextView text4 = mDialog.findViewById(R.id.txt4);
        TextView text5 = mDialog.findViewById(R.id.txt5);
        TextView text6 = mDialog.findViewById(R.id.txt6);
        TextView text7 = mDialog.findViewById(R.id.txt7);
        TextView text8 = mDialog.findViewById(R.id.txt8);
        TextView text9 = mDialog.findViewById(R.id.txt9);
        TextView text10 = mDialog.findViewById(R.id.txt10);
        TextView text11 = mDialog.findViewById(R.id.txt11);
        TextView text12 = mDialog.findViewById(R.id.txt12);
        TextView text13 = mDialog.findViewById(R.id.txt13);
        TextView text14 = mDialog.findViewById(R.id.txt14);
        TextView text15 = mDialog.findViewById(R.id.txt15);
        TextView text16 = mDialog.findViewById(R.id.txt16);
        TextView text17 = mDialog.findViewById(R.id.txt17);
        TextView text18 = mDialog.findViewById(R.id.txt18);
        TextView text19 = mDialog.findViewById(R.id.txt19);
        TextView text20 = mDialog.findViewById(R.id.txt20);
        TextView text21 = mDialog.findViewById(R.id.txt21);
        TextView text22 = mDialog.findViewById(R.id.txt22);
        TextView text23 = mDialog.findViewById(R.id.txt23);
        TextView text24 = mDialog.findViewById(R.id.txt24);
        TextView text25 = mDialog.findViewById(R.id.txt25);
        TextView text26 = mDialog.findViewById(R.id.txt26);
        ImageView cancel_btn = mDialog.findViewById(R.id.cancel);
        Button btn_okay = mDialog.findViewById(R.id.okay);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        text1.setText(txt1);
        text2.setText(txt2);
        text3.setText(txt3);
        text4.setText(txt4);
        text5.setText(txt5);
        text6.setText(txt6);
        text7.setText(txt7);
        text8.setText(txt8);
        text9.setText(txt9);
        text10.setText(txt10);
        text11.setText(txt11);
        text12.setText(txt12);
        text13.setText(txt13);
        text14.setText(txt14);
        text15.setText(txt15);
        text16.setText(txt16);
        text17.setText(txt17);
        text18.setText(txt18);
        text19.setText(txt19);
        text20.setText(txt20);
        text21.setText(txt21);
        text22.setText(txt22);
        text23.setText(txt23);
        text24.setText(txt24);
        text25.setText(txt25);
        text26.setText(txt26);
        mDialog.show();
    }
}

