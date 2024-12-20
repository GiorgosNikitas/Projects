import * as React from 'react';
import {extendTheme} from '@mui/material/styles';
import {AppProvider} from '@toolpad/core/AppProvider';
import {DashboardLayout} from '@toolpad/core/DashboardLayout';
import {PageContainer} from '@toolpad/core/PageContainer';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import SchoolIcon from '@mui/icons-material/School';
import BookIcon from '@mui/icons-material/Book';
import BarChartIcon from '@mui/icons-material/BarChart';
import AssignmentTurnedInIcon from '@mui/icons-material/AssignmentTurnedIn';

// Pages
import General from './General';
import Courses from './Courses';
import Students from './Students';
import Assignment from './Assignment';
import Statistics from './Statistics';

const NAVIGATION = [
    { kind: 'header', title: 'Content' },
    { segment: 'general', title: 'General', icon: <FileUploadIcon /> },
    { segment: 'courses', title: 'Courses', icon: <BookIcon /> },
    { segment: 'students', title: 'Students', icon: <SchoolIcon /> },
    { segment: 'assignment', title: 'Assignment', icon: <AssignmentTurnedInIcon /> },
    { segment: 'statistics', title: 'Statistics', icon: <BarChartIcon /> },
];

const demoTheme = extendTheme({
    colorSchemes: { light: true, dark: true },
    colorSchemeSelector: 'class',
    breakpoints: { values: { xs: 0, sm: 600, md: 600, lg: 1200, xl: 1536 } },
});

function useDemoRouter(initialPath) {
    const [pathname, setPathname] = React.useState(initialPath);

    return React.useMemo(() => ({
        pathname,
        searchParams: new URLSearchParams(),
        navigate: (path) => setPathname(String(path)),
    }), [pathname]);
}

export default function MyNavbar() {
    const router = useDemoRouter('/general');

    const renderPageContent = () => {
        switch (router.pathname) {
            case '/general':
                return <General />;
            case '/courses':
                return <Courses />;
            case '/students':
                return <Students />;
            case '/assignment':
                return <Assignment />;
            case '/statistics':
                return <Statistics />;
            default:
                return <div>Page Not Found</div>;
        }
    };

    return (
        <AppProvider
            navigation={NAVIGATION}
            router={router}
            theme={demoTheme}
            branding={{
                title: 'Taid',
            }}>
            <DashboardLayout>
                {renderPageContent()}
            </DashboardLayout>
        </AppProvider>
    );
}
